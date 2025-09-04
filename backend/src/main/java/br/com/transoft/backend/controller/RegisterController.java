package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.RegistrationAdminDto;
import br.com.transoft.backend.dto.RegistrationDto;
import br.com.transoft.backend.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/register")
public class RegisterController {

    private final RegistrationService registrationService;

    public RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegistrationDto registrationDto) {
        registrationService.register(registrationDto);
    }

    @PostMapping
    @RequestMapping(path = "/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdmin(@Valid @RequestBody RegistrationAdminDto registrationAdminDto) {
        registrationService.registerAdmin(registrationAdminDto);
    }

}
