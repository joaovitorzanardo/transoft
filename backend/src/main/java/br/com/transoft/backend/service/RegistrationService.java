package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.registration.RegistrationAdminDto;
import br.com.transoft.backend.dto.registration.RegistrationDto;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.EmailNotFromEmployeeException;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.mapper.UserAccountMapper;
import br.com.transoft.backend.repository.CompanyRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Pattern;

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
        boolean isCnpjRegistered = companyRepository.findByCnpj(registrationDto.getCompany().getCnpj()).isPresent();

        if (isEmailInUse(registrationDto.getEmail())) {
            throw new ResourceConflictException("O email informado já está sendo utilizado!");
        }

        if (isCnpjRegistered) {
            throw new ResourceConflictException("Uma empresa com esse CNPJ já está cadastrada!");
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

    @Transactional(rollbackOn = SQLException.class)
    public void registerAdmin(RegistrationAdminDto registrationAdminDto) {
        if (isEmailInUse(registrationAdminDto.getEmail())) {
            throw new ResourceConflictException("O email informado já está sendo utilizado!");
        }

        if (!isEmailFromEmployee(registrationAdminDto.getEmail())) {
            throw new EmailNotFromEmployeeException(registrationAdminDto.getEmail());
        }

        UserAccount userAccount = UserAccountMapper.toSysAdminAccount(registrationAdminDto, passwordEncoder);

        userAccountRepository.save(userAccount);
    }

    private boolean isEmailInUse(String email) {
        return userAccountRepository.findByEmail(email).isPresent();
    }

    private boolean isEmailFromEmployee(String email) {
        Pattern pattern = Pattern.compile("^[^@\\s]+@transoft\\.com$");
        return pattern.matcher(email).matches();
    }

}
