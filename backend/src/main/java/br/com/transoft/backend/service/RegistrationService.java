package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.KeycloakRegistrationResponse;
import br.com.transoft.backend.dto.RegistrationDto;
import br.com.transoft.backend.dto.UserAccountDto;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.repository.CompanyRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RegistrationService {

    private final UserAccountRepository userAccountRepository;
    private final CompanyRepository companyRepository;

    private final RegistrationKeycloakFacade registrationKeycloakFacade;

    public RegistrationService(UserAccountRepository userAccountRepository, CompanyRepository companyRepository, RegistrationKeycloakFacade registrationKeycloakFacade) {
        this.userAccountRepository = userAccountRepository;
        this.companyRepository = companyRepository;
        this.registrationKeycloakFacade = registrationKeycloakFacade;
    }

    @Transactional(rollbackOn = {SQLException.class})
    public void register(RegistrationDto registrationDto) {
        boolean isEmailInUse = this.userAccountRepository.findByEmail(registrationDto.getEmail()).isPresent();
        boolean isCnpjRegistered = this.companyRepository.findByCnpj(registrationDto.getCompany().getCnpj()).isPresent();

        if (isEmailInUse) {
            throw new ResourceConflictException("Email already in use");
        }

        if (isCnpjRegistered) {
            throw new ResourceConflictException("A company with this CNPJ is already registered");
        }

        KeycloakRegistrationResponse keycloakRegistrationResponse = registrationKeycloakFacade
                .registerKeycloak(
                        new UserAccountDto(registrationDto.getName(), registrationDto.getEmail(), registrationDto.getPassword(), List.of("MANAGER")),
                        registrationDto.getCompany()
                );

        UserAccount userAccount = UserAccount.builder()
                .userAccountId(keycloakRegistrationResponse.userId())
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .role("MANAGER")
                .active(true)
                .build();

        this.userAccountRepository.save(userAccount);

        Company company = Company.builder()
                .companyId(keycloakRegistrationResponse.companyId())
                .name(registrationDto.getCompany().getName())
                .email(registrationDto.getCompany().getEmail())
                .cnpj(registrationDto.getCompany().getCnpj())
                .build();

        this.companyRepository.save(company);
    }

}
