package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.driver.DriverDto;
import br.com.transoft.backend.entity.Driver;
import br.com.transoft.backend.entity.PhoneNumber;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.CnhExpirationException;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.repository.DriverRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.passay.Rule;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserAccountRepository userAccountRepository;
    private final KeycloakService keycloakService;
    private final PasswordGenerator passwordGenerator;

    public DriverService(DriverRepository driverRepository, UserAccountRepository userAccountRepository, KeycloakService keycloakService) {
        this.driverRepository = driverRepository;
        this.userAccountRepository = userAccountRepository;
        this.keycloakService = keycloakService;
        this.passwordGenerator = new PasswordGenerator();
    }

    @Transactional(rollbackOn = {SQLException.class})
    public void saveDriver(DriverDto driverDto) {
        //TODO: Get companyId from the JWT token and set on the driver object
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
                .cnhNumber(driverDto.getCnhNumber())
                .cnhExpirationDate(driverDto.getCnhExpirationDate())
                .phoneNumber(new PhoneNumber(driverDto.getPhoneNumber()))
                .build();

        this.driverRepository.save(driver);

        List<Rule> rules = List.of(
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1)
        );

        String userId = keycloakService.createUser(driverDto.getName(), driverDto.getEmail(), passwordGenerator.generatePassword(6, rules), false);

        UserAccount userAccount = UserAccount.builder()
                .userAccountId(userId)
                .name(driverDto.getName())
                .email(driverDto.getEmail())
                .role("DRIVER")
                .active(false)
                .build();

        //TODO: send email to the user with a password

        userAccountRepository.save(userAccount);
    }

    public boolean cnhNumberRegistered(String cnhNumber) {
        return this.driverRepository.findByCnhNumber(cnhNumber).isPresent();
    }

    public boolean driverEmailRegistered(String email) {
        return this.driverRepository.findByEmail(email).isPresent();
    }

    private boolean isCnhExpired(LocalDate cnhExpirationDate) {
        return LocalDate.now().isEqual(cnhExpirationDate) || LocalDate.now().isAfter(cnhExpirationDate);
    }

}
