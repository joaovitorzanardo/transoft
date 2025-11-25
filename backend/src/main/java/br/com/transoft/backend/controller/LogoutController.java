package br.com.transoft.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/logout")
public class LogoutController {

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "Authorization")
    public Response logout(HttpServletRequest request, HttpServletResponse response) {
        extractTokenFromRequestHeader(request).ifPresent((cookie) -> {
                cookie.setMaxAge(0);
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        });

        return Response.ok().build();
    }

    private Optional<Cookie> extractTokenFromRequestHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }
}
