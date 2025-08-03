package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.RegistrationDto;
import br.com.transoft.backend.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/register")
public class RegisterController {

    private final RegistrationService registrationService;

    public RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public void register(@Valid @RequestBody RegistrationDto registrationDto) {
        this.registrationService.register(registrationDto);
    }

}
