package br.com.transoft.backend.controller;

import br.com.transoft.backend.dto.auth.TokenRequest;
import br.com.transoft.backend.dto.auth.TokenResponse;
import br.com.transoft.backend.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/refresh-token")
public class RefreshTokenController {

    private final TokenService tokenService;

    public RefreshTokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse getRefreshToken(@RequestBody TokenRequest tokenRequest) {
        return tokenService.refreshToken(tokenRequest);
    }

}
