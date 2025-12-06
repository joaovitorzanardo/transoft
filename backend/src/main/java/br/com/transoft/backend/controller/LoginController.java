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
            ResponseCookie tokenCookie = ResponseCookie.from("token", loginResponse.token())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(900000)
                    .sameSite("Lax")
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", loginResponse.refreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(604800000)
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        }

        return loginResponse;
    }
}
