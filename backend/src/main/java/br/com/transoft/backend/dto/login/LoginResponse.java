package br.com.transoft.backend.dto.login;

public record LoginResponse(UserAccountDto user, String token, String refreshToken) {
}
