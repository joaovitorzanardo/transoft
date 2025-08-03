package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.RegistrationDto;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.KeycloakApiException;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.CompanyRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class RegistrationService {

    private final UserAccountRepository userAccountRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    private final KeycloakService keycloakService;

    public RegistrationService(UserAccountRepository userAccountRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder, KeycloakService keycloakService) {
        this.userAccountRepository = userAccountRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.keycloakService = keycloakService;
    }

    @Transactional(rollbackOn = {SQLException.class, KeycloakApiException.class})
    public void register(RegistrationDto registrationDto) {
        boolean isEmailInUse = this.userAccountRepository.findByEmail(registrationDto.getEmail()).isPresent();
        boolean isCnpjRegistered = this.companyRepository.findByCnpj(registrationDto.getCompanyCnpj()).isPresent();

        if (isEmailInUse) {
            throw new ResourceConflictException("Email already in use");
        }

        if (isCnpjRegistered) {
            throw new ResourceConflictException("A company with this CNPJ is already registered");
        }

        UserAccount userAccount = UserAccount.builder()
                .userAccountId(UUID.randomUUID().toString())
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(this.passwordEncoder.encode(registrationDto.getPassword()))
                .role("ADMIN")
                .build();

        this.userAccountRepository.save(userAccount);

        Company company = Company.builder()
                .companyId(UUID.randomUUID().toString())
                .name(registrationDto.getCompanyName())
                .cnpj(registrationDto.getCompanyCnpj())
                .build();

        this.companyRepository.save(company);

        Response createOrgResponse = keycloakService.createOrganization(company.getCompanyId(), company.getName());

        if (createOrgResponse.getStatus() != 201) {
            throw new KeycloakApiException(createOrgResponse.readEntity(String.class));
        }

    }

}
