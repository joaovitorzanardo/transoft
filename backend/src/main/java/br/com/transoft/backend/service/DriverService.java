package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.dto.driver.DriverPresenterList;
import br.com.transoft.backend.dto.driver.DriverStatsPresenter;
import br.com.transoft.backend.dto.driver.account.DriverAccountDto;
import br.com.transoft.backend.entity.*;
import br.com.transoft.backend.entity.route.Route;
import br.com.transoft.backend.mapper.UserAccountMapper;
import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.exception.CnhExpirationException;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.DriverRepository;
import br.com.transoft.backend.repository.ItineraryRepository;
import br.com.transoft.backend.repository.RouteRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import br.com.transoft.backend.utils.PasswordGeneratorUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final RouteRepository routeRepository;
    private final ItineraryRepository itineraryRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public DriverService(DriverRepository driverRepository,
                         RouteRepository routeRepository,
                         ItineraryRepository itineraryRepository,
                         UserAccountRepository userAccountRepository,
                         PasswordEncoder passwordEncoder,
                         EmailService emailService) {
        this.driverRepository = driverRepository;
        this.routeRepository = routeRepository;
        this.itineraryRepository = itineraryRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional(rollbackOn = SQLException.class)
    public void saveDriver(DriverDto driverDto, LoggedUserAccount loggedUserAccount) {
        if (cnhNumberRegistered(driverDto.getCnhNumber(), loggedUserAccount.companyId())) {
            throw new ResourceConflictException("O número da CNH já está registrado para outro motorista!");
        }

        if (driverEmailRegistered(driverDto.getEmail())) {
            throw new ResourceConflictException("O email informado já está registrado para outro motorista!");
        }

        if (isCnhExpired(driverDto.getCnhExpirationDate())) {
            throw new CnhExpirationException();
        }

        String password = PasswordGeneratorUtils.generatePassword();

        UserAccount userAccount = UserAccountMapper.toDriverAccount(driverDto, passwordEncoder.encode(password), new Company(loggedUserAccount.companyId()));

        Driver driver = Driver.builder()
                .driverId(UUID.randomUUID().toString())
                .name(driverDto.getName())
                .email(driverDto.getEmail())
                .cnhNumber(driverDto.getCnhNumber())
                .cnhExpirationDate(driverDto.getCnhExpirationDate())
                .phoneNumber(new PhoneNumber(driverDto.getPhoneNumber()))
                .company(new Company(loggedUserAccount.companyId()))
                .userAccount(userAccount)
                .build();

        driverRepository.save(driver);
        emailService.sendEmailWithUserPassword(userAccount.getEmail(), password);
    }

    public void updateDriverAccount(DriverAccountDto driverAccountDto, LoggedUserAccount loggedUserAccount) {
        Driver driver = driverRepository
                .findByUserAccount_UserAccountId(loggedUserAccount.userAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado"));

        if (!Objects.equals(driver.getEmail(), driverAccountDto.getEmail())) {
            if (driverRepository.findByEmail(driverAccountDto.getEmail()).isPresent()) {
                throw new ResourceConflictException("O email informado já está registrado para outro motorista!");
            }

            driver.setEmail(driverAccountDto.getEmail());
        }

        driver.setName(driverAccountDto.getName());
        driver.setPhoneNumber(new PhoneNumber(driverAccountDto.getPhoneNumber()));

        driverRepository.save(driver);
    }

    public DriverPresenter getDriverAccount(LoggedUserAccount loggedUserAccount) {
        return driverRepository
                .findByUserAccount_UserAccountId(loggedUserAccount.userAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado"))
                .toPresenter();
    }

    public Driver findDriverByUserAccountId(String userAccountId) {
        return driverRepository
                .findByUserAccount_UserAccountId(userAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado"));
    }

    public void enableDriver(String driverId, LoggedUserAccount loggedUserAccount) {
        Driver driver = findDriverById(driverId, loggedUserAccount);
        UserAccount userAccount = driver.getUserAccount();
        userAccount.setEnabled(true);
        userAccountRepository.save(userAccount);
    }

    public void disableDriver(String driverId, LoggedUserAccount loggedUserAccount) {
        Driver driver = findDriverById(driverId, loggedUserAccount);

        List<Route> routes = routeRepository
                .findAllRoutesByCompany_CompanyIdAndDefaultDriver_DriverId(loggedUserAccount.companyId(), driverId);

        if (!routes.isEmpty()) {
            throw new ResourceConflictException("Não foi possível desabilitar o motorista! Esse motorista está cadastrado como motorista padrão para uma rota.");
        }

        List<Itinerary> itineraries = itineraryRepository
                .findAllScheduledAndOngoingItinerariesByDriverId(loggedUserAccount.companyId(), driverId);

        if (!itineraries.isEmpty()) {
            throw new ResourceConflictException("Não foi possível desabilitar o motorista! Esse motorista está sendo utilizado em itinerários com o status AGENDADO ou EM ANDAMENTO.");
        }

        UserAccount userAccount = driver.getUserAccount();
        userAccount.setEnabled(false);
        userAccountRepository.save(userAccount);
    }

    public DriverStatsPresenter getDriverStats(LoggedUserAccount loggedUserAccount) {
        int total = driverRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());
        int active = driverRepository.countAllByCompany_CompanyIdAndUserAccount_ActiveAndUserAccount_Enabled(loggedUserAccount.companyId(), true, true);
        int inactive = driverRepository.countAllByCompany_CompanyIdAndUserAccount_ActiveAndUserAccount_Enabled(loggedUserAccount.companyId(), true, false);
        int pending = driverRepository.countAllByCompany_CompanyIdAndUserAccount_Active(loggedUserAccount.companyId(), false);

        return new DriverStatsPresenter(total, active, inactive, pending);
    }

    public boolean cnhNumberRegistered(String cnhNumber, String companyId) {
        return driverRepository.findByCnhNumberAndCompany_CompanyId(cnhNumber, companyId).isPresent();
    }

    public boolean driverEmailRegistered(String email) {
        return driverRepository.findByEmail(email).isPresent();
    }

    public DriverPresenterList listDrivers(int page, int size, LoggedUserAccount loggedUserAccount) {
        List<DriverPresenter> drivers = driverRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId(), PageRequest.of(page, size))
                .stream()
                .map(Driver::toPresenter).toList();

        int count = driverRepository.countAllByCompany_CompanyId(loggedUserAccount.companyId());
        return new DriverPresenterList(count, drivers);
    }

    public List<DriverPresenter> listDrivers(LoggedUserAccount loggedUserAccount) {
        return driverRepository.findAllByCompany_CompanyId(loggedUserAccount.companyId())
                .stream()
                .map(Driver::toPresenter).toList();
    }

    public List<DriverPresenter> listAllEnabledDrivers(LoggedUserAccount loggedUserAccount) {
        return driverRepository.findAllByCompany_CompanyIdAndUserAccountEnabledTrue(loggedUserAccount.companyId())
                .stream()
                .map(Driver::toPresenter).toList();
    }

    public Driver findDriverById(String driverId, LoggedUserAccount loggedUserAccount) {
        return driverRepository.findByDriverIdAndCompany_CompanyId(driverId, loggedUserAccount.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado"));
    }

    public void updateDriver(String driverId, DriverDto driverDto, LoggedUserAccount loggedUserAccount) {
        Driver driver = findDriverById(driverId, loggedUserAccount);

        driver.setName(driverDto.getName());

        if (!driver.getCnhNumber().equals(driverDto.getCnhNumber())) {
            if (cnhNumberRegistered(driverDto.getCnhNumber(), loggedUserAccount.companyId())) {
                throw new ResourceConflictException("O número da CNH já está registrado para outro motorista!");
            }

            driver.setCnhNumber(driverDto.getCnhNumber());
        }

        if (!driver.getEmail().equals(driverDto.getEmail())) {
            if (driverEmailRegistered(driverDto.getEmail())) {
                throw new ResourceConflictException("O email informado já está registrado para outro motorista!");
            }

            driver.setEmail(driverDto.getEmail());
        }

        if (isCnhExpired(driverDto.getCnhExpirationDate())) {
            throw new CnhExpirationException();
        }

        driver.setCnhExpirationDate(driverDto.getCnhExpirationDate());
        driver.setPhoneNumber(driverDto.getPhoneNumber().toEntity());

        driverRepository.save(driver);
    }

    private boolean isCnhExpired(LocalDate cnhExpirationDate) {
        return LocalDate.now().isEqual(cnhExpirationDate) || LocalDate.now().isAfter(cnhExpirationDate);
    }

}
