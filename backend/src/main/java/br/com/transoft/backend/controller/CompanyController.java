package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.CompanyDto;
import br.com.transoft.backend.service.CompanyService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PutMapping(path = "/{companyId}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateCompany(@PathVariable String companyId, CompanyDto companyDto) {
        companyService.updateCompany(companyId, companyDto);
    }

}
