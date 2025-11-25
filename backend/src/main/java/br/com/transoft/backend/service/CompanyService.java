package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.CompanyDto;
import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.entity.UserAccount;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.CompanyRepository;
import br.com.transoft.backend.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserAccountRepository userAccountRepository;

    public CompanyService(CompanyRepository companyRepository, UserAccountRepository userAccountRepository) {
        this.companyRepository = companyRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public CompanyDto getCompany(LoggedUserAccount loggedUserAccount) {
        return companyRepository
                .findById(loggedUserAccount.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"))
                .toDto();
    }

    public void updateCompany(CompanyDto companyDto, LoggedUserAccount loggedUserAccount) {
        Company company = companyRepository.findById(loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        UserAccount userAccount = userAccountRepository.findById(loggedUserAccount.userAccountId()).orElseThrow(() -> new ResourceNotFoundException("UserAccount not found"));

        company.setName(companyDto.getName());
        userAccount.setName(companyDto.getName());

        if (!company.getEmail().equals(companyDto.getEmail())) {
            if (isEmailRegistered(companyDto.getEmail())) {
                throw new ResourceConflictException("Email already registered for another company.");
            }
            company.setEmail(companyDto.getEmail());
            userAccount.setEmail(companyDto.getEmail());
        }

        companyRepository.save(company);
        userAccountRepository.save(userAccount);
    }

    private boolean isEmailRegistered(String email) {
        return companyRepository.findByEmail(email).isPresent();
    }

}
