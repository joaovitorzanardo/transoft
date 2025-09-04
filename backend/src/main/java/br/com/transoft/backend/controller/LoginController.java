package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.login.LoginDto;
import br.com.transoft.backend.dto.login.LoginResponse;
import br.com.transoft.backend.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginDto loginDto) {
        return loginService.login(loginDto);
    }

}
