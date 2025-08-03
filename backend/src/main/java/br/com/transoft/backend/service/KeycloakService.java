package br.com.transoft.backend.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.OrganizationDomainRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.springframework.stereotype.Service;

@Service
public class KeycloakService {

    private final Keycloak keycloak;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public Response createOrganization(String id, String name) {
        String treatedName = name.replaceAll("\\s+", "").toLowerCase();
        OrganizationRepresentation organization = new OrganizationRepresentation();

        organization.setId(id);
        organization.setName(treatedName);
        organization.setEnabled(true);

        organization.addDomain(new OrganizationDomainRepresentation("br.com." + treatedName));

        return keycloak.realm("master").organizations().create(organization);
    }

}
