package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.LoginDto;
import br.com.transoft.backend.dto.LoginResponse;
import br.com.transoft.backend.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginDto loginDto) {
        return loginService.login(loginDto);
    }

}
