package br.com.transoft.backend.dto.login;

public record UserAccountDto(String name, String email, boolean active, String role) {
}
