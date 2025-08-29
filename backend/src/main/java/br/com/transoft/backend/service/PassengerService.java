package br.com.transoft.backend.service;

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
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final SchoolService schoolService;
    private final CoordinateService coordinateService;
    private final RouteService routeService;
    private final PasswordEncoder passwordEncoder;

    public PassengerService(PassengerRepository passengerRepository, SchoolService schoolService, CoordinateService coordinateService, RouteService routeService, PasswordEncoder passwordEncoder) {
        this.passengerRepository = passengerRepository;
        this.schoolService = schoolService;
        this.coordinateService = coordinateService;
        this.routeService = routeService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackOn = {SQLException.class})
    public void savePassenger(PassengerDto passengerDto) {
        if (isEmailRegistered(passengerDto.getEmail())) {
            throw new ResourceConflictException("This email is already registered for another passenger");
        }

        Address address = AddressMapper.toEntity(
                passengerDto.getAddress(),
                coordinateService.findCoordinateByAddress(passengerDto.getAddress().toString())
        );

        UserAccount userAccount = UserAccountMapper.toPassengerAccount(passengerDto, passwordEncoder);

        School school = schoolService.findSchoolById(passengerDto.getSchoolId());

        passengerRepository.save(PassengerMapper.toEntity(passengerDto, address, school, userAccount));

        //TODO: send email to the user with a password
    }

    public List<PassengerPresenter> listPassengersFromRoute(String routeId) {
        Route route = routeService.findRouteById(routeId);
        return passengerRepository.findByRoute(route).stream().map(Passenger::toPresenter).collect(Collectors.toList());
    }

    public void updatePassengerAccount(PassengerAccountDto passengerAccountDto) {

    }

    public PassengerPresenter getPassengerAccount() {
        return null;
    }

    public List<PassengerPresenter> listPassengers(int page, int size) {
        return passengerRepository.findAll(PageRequest.of(page, size)).stream().map(Passenger::toPresenter).collect(Collectors.toList());
    }

    public Passenger findPassengerById(String passengerId) {
        return passengerRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger was not found."));
    }

    public void updatePassenger(String passengerId, PassengerDto passengerDto) {
        Passenger passenger = findPassengerById(passengerId);

        passenger.setName(passengerDto.getName());

        if (!passenger.getEmail().equals(passengerDto.getEmail())) {
            if (isEmailRegistered(passengerDto.getEmail())) {
                throw new ResourceConflictException("Email is already in use.");
            }
            passenger.setEmail(passengerDto.getEmail());
        }

        passenger.setPhoneNumber(new PhoneNumber(passenger.getPhoneNumber().getDdd(), passenger.getPhoneNumber().getNumber()));

        if (!passenger.getSchool().getSchoolId().equals(passengerDto.getSchoolId())) {
            School newSchool = schoolService.findSchoolById(passengerDto.getSchoolId());
            passenger.setSchool(newSchool);
        }

        Address address = AddressMapper.toEntity(
                passengerDto.getAddress(),
                coordinateService.findCoordinateByAddress(passengerDto.getAddress().toString())
        );

        passenger.setAddress(address);
        passengerRepository.save(passenger);
    }

    public void enablePassenger(String passengerId) {

    }

    public void disablePassenger(String passengerId) {

    }

    private boolean isEmailRegistered(String email) {
        return passengerRepository.findByEmail(email).isPresent();
    }

}
