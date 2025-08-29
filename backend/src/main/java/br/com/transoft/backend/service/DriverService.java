package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.driver.account.DriverAccountDto;
import br.com.transoft.backend.dto.driver.account.DriverAccountPresenter;
import br.com.transoft.backend.mapper.UserAccountMapper;
import br.com.transoft.backend.utils.PasswordGeneratorUtils;
import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.dto.driver.DriverPresenter;
import br.com.transoft.backend.entity.Driver;
import br.com.transoft.backend.entity.PhoneNumber;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.CnhExpirationException;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.DriverRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.passay.PasswordGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverService(DriverRepository driverRepository, UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackOn = {SQLException.class})
    public void saveDriver(DriverDto driverDto) {
        if (cnhNumberRegistered(driverDto.getCnhNumber())) {
            throw new ResourceConflictException("CNH number already registered");
        }

        if (driverEmailRegistered(driverDto.getEmail())) {
            throw new ResourceConflictException("Email already registered");
        }

        if (isCnhExpired(driverDto.getCnhExpirationDate())) {
            throw new CnhExpirationException();
        }

        Driver driver = Driver.builder()
                .driverId(UUID.randomUUID().toString())
                .name(driverDto.getName())
                .email(driverDto.getEmail())
                .cnhNumber(driverDto.getCnhNumber())
                .cnhExpirationDate(driverDto.getCnhExpirationDate())
                .phoneNumber(new PhoneNumber(driverDto.getPhoneNumber()))
                .build();

        driverRepository.save(driver);

        UserAccount userAccount = UserAccountMapper.toDriverAccount(driverDto, passwordEncoder);

        userAccountRepository.save(userAccount);
    }

    public void updateDriverAccount(DriverAccountDto driverAccountDto) {

    }

    public DriverAccountPresenter getDriverAccount() {
        return null;
    }

    public boolean cnhNumberRegistered(String cnhNumber) {
        return driverRepository.findByCnhNumber(cnhNumber).isPresent();
    }

    public boolean driverEmailRegistered(String email) {
        return this.driverRepository.findByEmail(email).isPresent();
    }

    public List<DriverPresenter> listDrivers(int page, int size) {
        return driverRepository.findAll(PageRequest.of(page, size)).stream().map(Driver::toPresenter).toList();
    }

    public Driver findDriverById(String driverId) {
        return driverRepository.findById(driverId).orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    }

    public void updateDriver(String driverId, DriverDto driverDto) {
        Driver driver = findDriverById(driverId);

        driver.setName(driverDto.getName());

        if (!driver.getCnhNumber().equals(driverDto.getCnhNumber())) {
            if (cnhNumberRegistered(driverDto.getCnhNumber())) {
                throw new ResourceConflictException("CNH number already registered");
            }

            driver.setCnhNumber(driverDto.getCnhNumber());
        }

        if (!driver.getEmail().equals(driverDto.getEmail())) {
            if (driverEmailRegistered(driverDto.getEmail())) {
                throw new ResourceConflictException("Email already registered");
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
