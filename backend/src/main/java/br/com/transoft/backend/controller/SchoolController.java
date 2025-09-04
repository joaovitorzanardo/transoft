package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.school.SchoolDto;
import br.com.transoft.backend.dto.school.SchoolPresenter;
import br.com.transoft.backend.service.SchoolService;
import jakarta.validation.Valid;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSchool(@Valid @RequestBody SchoolDto schoolDto) {
        schoolService.saveSchool(schoolDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public List<SchoolPresenter> listSchools(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return schoolService.listSchools(page, size);
    }

    @GetMapping(path = "/{schoolId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'MANAGER')")
    @SecurityRequirement(name = "Authorization")
    @ResponseStatus(HttpStatus.OK)
    public SchoolPresenter findSchoolById(@PathVariable String schoolId) {
        return schoolService.findSchoolById(schoolId).toPresenter();
    }

}
