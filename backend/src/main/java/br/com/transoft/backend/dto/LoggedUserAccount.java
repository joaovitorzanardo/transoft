package br.com.transoft.backend.dto;

import br.com.transoft.backend.constants.Role;

public record LoggedUserAccount (
        String userAccountId,
        String companyId,
        Role role
) {}
