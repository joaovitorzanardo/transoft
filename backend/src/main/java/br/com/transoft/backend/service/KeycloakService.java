package br.com.transoft.backend.service;

import com.github.slugify.Slugify;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.OrganizationResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.OrganizationDomainRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakService {

    private final Keycloak keycloak;
    private final Slugify slugify;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
        this.slugify = Slugify.builder().build();
    }

    public String createOrganization(String name) {
        OrganizationRepresentation organization = new OrganizationRepresentation();

        organization.setName(name);
        organization.setAlias(slugify.slugify(name));
        organization.setEnabled(true);

        organization.addDomain(new OrganizationDomainRepresentation("br.com." + organization.getAlias()));

        Response createOrgResponse = keycloak.realm("master").organizations().create(organization);

        String location = createOrgResponse.getLocation().getPath();
        return location.substring(location.lastIndexOf('/') + 1);
    }

    public String createUser(String name, String email, String password, boolean enabled) {
        UserRepresentation user = new UserRepresentation();

        user.setUsername(slugify.slugify(name));
        user.setFirstName(name);
        user.setEmail(email);
        user.setEnabled(enabled);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        Response createUserResponse = keycloak.realm("master").users().create(user);

        String location = createUserResponse.getLocation().getPath();
        return location.substring(location.lastIndexOf('/') + 1);
    }

    public Response addUserToOrganization(String userId, String orgId) {
        OrganizationResource organization = keycloak.realm("master")
                .organizations()
                .get(orgId);

        return organization.members().addMember(userId);
    }

}
