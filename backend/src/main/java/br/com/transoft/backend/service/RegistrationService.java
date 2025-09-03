package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.RegistrationDto;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.mapper.UserAccountMapper;
import br.com.transoft.backend.repository.CompanyRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class RegistrationService {

    private final UserAccountRepository userAccountRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserAccountRepository userAccountRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackOn = {SQLException.class})
    public void register(RegistrationDto registrationDto) {
        boolean isEmailInUse = userAccountRepository.findByEmail(registrationDto.getEmail()).isPresent();
        boolean isCnpjRegistered = companyRepository.findByCnpj(registrationDto.getCompany().getCnpj()).isPresent();

        if (isEmailInUse) {
            throw new ResourceConflictException("Email already in use");
        }

        if (isCnpjRegistered) {
            throw new ResourceConflictException("A company with this CNPJ is already registered");
        }

        Company company = Company.builder()
                .companyId(UUID.randomUUID().toString())
                .name(registrationDto.getCompany().getName())
                .email(registrationDto.getCompany().getEmail())
                .cnpj(registrationDto.getCompany().getCnpj())
                .build();

        companyRepository.save(company);

        UserAccount userAccount = UserAccountMapper.toManagerAccount(registrationDto, passwordEncoder, company);

        userAccountRepository.save(userAccount);

    }

}
