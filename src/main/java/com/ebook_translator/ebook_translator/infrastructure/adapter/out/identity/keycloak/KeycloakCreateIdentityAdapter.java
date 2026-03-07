package com.ebook_translator.ebook_translator.infrastructure.adapter.out.identity.keycloak;

import com.ebook_translator.ebook_translator.application.port.out.CreateIdentityPort;
import com.ebook_translator.ebook_translator.configuration.property.KeycloakProperties;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeycloakCreateIdentityAdapter implements CreateIdentityPort {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public KeycloakCreateIdentityAdapter(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public void createIdentity(String login, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(login);
        user.setEmail(login);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(keycloakProperties.getRealm())
                .users()
                .create(user);

        if (response.getStatus() == 201) {
            return;
        }

        if (response.getStatus() == 409) {
            throw new RuntimeException("Utilisateur déjà existant");
        }

        throw new RuntimeException("Erreur lors de l'inscription");
    }
}
