package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.CompanyDto;
import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.entity.Company;
import br.com.transoft.backend.exception.ResourceConflictException;
import br.com.transoft.backend.exception.ResourceNotFoundException;
import br.com.transoft.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyDto getCompany(LoggedUserAccount loggedUserAccount) {
        return companyRepository
                .findById(loggedUserAccount.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"))
                .toDto();
    }

    public void updateCompany(CompanyDto companyDto, LoggedUserAccount loggedUserAccount) {
        Company company = companyRepository.findById(loggedUserAccount.companyId()).orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        company.setName(companyDto.getName());

        if (!company.getEmail().equals(companyDto.getEmail())) {
            if (isEmailRegistered(companyDto.getEmail())) {
                throw new ResourceConflictException("Email already registered for another company.");
            }
            company.setEmail(companyDto.getEmail());
        }

        companyRepository.save(company);
    }

    private boolean isEmailRegistered(String email) {
        return companyRepository.findByEmail(email).isPresent();
    }

}
