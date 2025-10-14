package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.CompanyDto;
import br.com.transoft.backend.dto.LoggedUserAccount;
import br.com.transoft.backend.service.CompanyService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public CompanyDto getCompany(Authentication authentication) {
        return companyService.getCompany((LoggedUserAccount) authentication.getPrincipal());
    }

    @PatchMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public void updateCompany(@Valid @RequestBody CompanyDto companyDto, Authentication authentication) {
        companyService.updateCompany(companyDto, (LoggedUserAccount) authentication.getPrincipal());
    }

}
