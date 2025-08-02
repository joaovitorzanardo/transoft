package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.RegistrationDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/register")
public class RegisterController {

    @PostMapping
    public void register(@Valid @RequestBody RegistrationDto registrationDto) {

    }

}
