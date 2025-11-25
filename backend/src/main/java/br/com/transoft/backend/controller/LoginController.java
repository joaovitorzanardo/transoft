package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.login.LoginDto;
import br.com.transoft.backend.dto.login.LoginResponse;
import br.com.transoft.backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    public LoginResponse login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = loginService.login(loginDto);

        if ("web".equals(request.getHeader("X-Client-Type"))) {
            ResponseCookie cookie = ResponseCookie.from("jwt", loginResponse.token())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(86400)
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        return loginResponse;
    }
}
