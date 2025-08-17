package br.com.transoft.backend.dto;

import java.util.List;

public record UserAccountDto(String name, String email, String password, List<String> roles) {
}
