package br.com.transoft.backend.dto;

import br.com.transoft.backend.constants.Role;

public record TokenInfo(String userAccountId, String companyId, Role role) {
}
