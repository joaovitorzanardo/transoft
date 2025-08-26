package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.school.SchoolDto;
import br.com.transoft.backend.dto.school.SchoolPresenter;
import br.com.transoft.backend.service.SchoolService;
import jakarta.validation.Valid;
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
    public void saveSchool(@Valid @RequestBody SchoolDto schoolDto) {
        this.schoolService.saveSchool(schoolDto);
    }

    @GetMapping
    public List<SchoolPresenter> listSchools(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return this.schoolService.listSchools(page, size);
    }

    @GetMapping(path = "/{schoolId}")
    public SchoolPresenter findSchoolById(@PathVariable String schoolId) {
        return this.schoolService.findSchoolById(schoolId);
    }

}
