package br.com.transoft.backend.service;

import br.com.transoft.backend.dto.CompanyDto;
import br.com.transoft.backend.dto.KeycloakRegistrationResponse;
import br.com.transoft.backend.dto.UserAccountDto;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

@Service
public class RegistrationKeycloakFacade {

    private final KeycloakService keycloakService;

    public RegistrationKeycloakFacade(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    public KeycloakRegistrationResponse registerKeycloak(UserAccountDto user, CompanyDto company) {
        String orgId = keycloakService.createOrganization(company.getName());
        String userId = keycloakService.createUser(user.name(), user.email(), user.password(), true, user.roles());

        Response addUserToOrgResponse = keycloakService.addUserToOrganization(userId, orgId);

        return new KeycloakRegistrationResponse(userId, orgId);
    }

}
