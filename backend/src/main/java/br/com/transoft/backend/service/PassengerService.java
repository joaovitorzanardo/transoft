package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.passenger.PassengerPresenterList;
import br.com.transoft.backend.dto.passenger.PassengerStatsPresenter;
import br.com.transoft.backend.dto.passenger.account.PassengerAccountDto;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.mapper.AddressMapper;
import br.com.transoft.backend.mapper.PassengerMapper;
import br.com.transoft.backend.mapper.UserAccountMapper;
import br.com.transoft.backend.dto.passenger.PassengerDto;
import br.com.transoft.backend.dto.passenger.PassengerPresenter;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.PassengerRepository;
import br.com.transoft.backend.utils.PasswordGeneratorUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final CoordinateService coordinateService;
    private final RouteService routeService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PassengerService(PassengerRepository passengerRepository, CoordinateService coordinateService, RouteService routeService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.passengerRepository = passengerRepository;
        this.coordinateService = coordinateService;
        this.routeService = routeService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional(rollbackOn = {SQLException.class})
    public void savePassenger(PassengerDto passengerDto, LoggedUserAccount loggedUserAccount) {
        if (isEmailRegistered(passengerDto.getEmail())) {
            throw new ResourceConflictException("This email is already registered for another passenger");
        }

        Address address = AddressMapper.toEntity(
                passengerDto.getAddress(),
                coordinateService.findCoordinateByAddress(passengerDto.getAddress().toString())
        );

        String password = PasswordGeneratorUtils.generatePassword();

        UserAccount userAccount = UserAccountMapper.toPassengerAccount(passengerDto, passwordEncoder.encode(password), new Company(loggedUserAccount.companyId()));

        Route route = routeService.findRouteById(passengerDto.getRouteId(), loggedUserAccount);

        passengerRepository.save(PassengerMapper.toEntity(passengerDto, address, route, userAccount, new Company(loggedUserAccount.companyId())));

        emailService.sendEmailWithUserPassword(userAccount.getEmail(), password);
    }

    public List<PassengerPresenter> listPassengersFromRoute(String routeId, LoggedUserAccount loggedUserAccount) {
        Route route = routeService.findRouteById(routeId, loggedUserAccount);
        return passengerRepository.findByRoute(route).stream().map(Passenger::toPresenter).collect(Collectors.toList());
    }

    public void updatePassengerAccount(PassengerAccountDto passengerAccountDto, LoggedUserAccount loggedUserAccount) {
        Passenger passenger = passengerRepository
                .findByUserAccount_UserAccountId(loggedUserAccount.userAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        if (!Objects.equals(passenger.getEmail(), passengerAccountDto.getEmail())) {
            if (passengerRepository.findByEmail(passengerAccountDto.getEmail()).isPresent()) {
                throw new ResourceConflictException("This email is already registered for another passenger");
            }

            passenger.setEmail(passengerAccountDto.getEmail());
        }

        passenger.setName(passengerAccountDto.getName());
        passenger.setPhoneNumber(new PhoneNumber(passengerAccountDto.getPhoneNumber()));

        passengerRepository.save(passenger);
    }

    public PassengerPresenter getPassengerAccount(LoggedUserAccount loggedUserAccount) {
        return passengerRepository
                .findByUserAccount_UserAccountId(loggedUserAccount.userAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"))
                .toPresenter();
    }

    public PassengerPresenterList listPassengers(int page, int size, LoggedUserAccount loggedUserAccount) {
        List<PassengerPresenter> passengers = passengerRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId(), PageRequest.of(page, size)).stream().map(Passenger::toPresenter).collect(Collectors.toList());
        int count = passengerRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());

        return new PassengerPresenterList(count, passengers);
    }

    public PassengerStatsPresenter getPassengersStats(LoggedUserAccount loggedUserAccount) {
        int total = passengerRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());
        int active = passengerRepository.countAllByCompany_CompanyIdAndUserAccount_ActiveAndUserAccount_Enabled(loggedUserAccount.companyId(), true, true);
        int inactive = passengerRepository.countAllByCompany_CompanyIdAndUserAccount_ActiveAndUserAccount_Enabled(loggedUserAccount.companyId(), true, false);
        int pending = passengerRepository.countAllByCompany_CompanyIdAndUserAccount_Active(loggedUserAccount.companyId(), false);

        return new PassengerStatsPresenter(total, active, inactive, pending);
    }

    public Passenger findPassengerByUserAccountId(String userAccountId) {
        return passengerRepository
                .findByUserAccount_UserAccountId(userAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));
    }

    public Passenger findPassengerById(String passengerId, LoggedUserAccount loggedUserAccount) {
        return passengerRepository.findByPassengerIdAndCompany_CompanyId(passengerId, loggedUserAccount.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Passenger was not found."));
    }

    public void updatePassenger(String passengerId, PassengerDto passengerDto, LoggedUserAccount loggedUserAccount) {
        Passenger passenger = findPassengerById(passengerId, loggedUserAccount);

        passenger.setName(passengerDto.getName());

        if (!passenger.getEmail().equals(passengerDto.getEmail())) {
            if (isEmailRegistered(passengerDto.getEmail())) {
                throw new ResourceConflictException("Email is already in use.");
            }
            passenger.setEmail(passengerDto.getEmail());
        }

        passenger.setPhoneNumber(new PhoneNumber(passenger.getPhoneNumber().getDdd(), passenger.getPhoneNumber().getNumber()));

        if (!passenger.getRoute().getRouteId().equals(passengerDto.getRouteId())) {
            Route newRoute = routeService.findRouteById(passengerDto.getRouteId(), loggedUserAccount);
            passenger.setRoute(newRoute);
        }

        Address address = AddressMapper.toEntity(
                passengerDto.getAddress(),
                coordinateService.findCoordinateByAddress(passengerDto.getAddress().toString())
        );

        passenger.setAddress(address);
        passengerRepository.save(passenger);
    }

    public void enablePassenger(String passengerId, LoggedUserAccount loggedUserAccount) {
        Passenger passenger = findPassengerById(passengerId, loggedUserAccount);
        passenger.getUserAccount().setEnabled(true);
        passengerRepository.save(passenger);
    }

    public void disablePassenger(String passengerId, LoggedUserAccount loggedUserAccount) {
        Passenger passenger = findPassengerById(passengerId, loggedUserAccount);
        passenger.getUserAccount().setEnabled(false);
        passengerRepository.save(passenger);
    }

    private boolean isEmailRegistered(String email) {
        return passengerRepository.findByEmail(email).isPresent();
    }

}
