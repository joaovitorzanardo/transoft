package br.com.transoft.backend.controller;

import br.com.transoft.backend.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping(path = "/api/logout")
public class LogoutController {

    private final TokenService tokenService;

    public LogoutController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    public Response logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie tokenCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, String.valueOf(tokenCookie));
        response.addHeader(HttpHeaders.SET_COOKIE, String.valueOf(refreshTokenCookie));

        extractRefreshTokenFromRequestHeader(request).ifPresent(refreshToken -> tokenService.deleteRefreshToken(refreshToken.getValue()));

        return Response.ok().build();
    }

    private Optional<Cookie> extractRefreshTokenFromRequestHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if ("refresh-token".equals(cookie.getName()) && !cookie.getValue().isEmpty()) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }


}
