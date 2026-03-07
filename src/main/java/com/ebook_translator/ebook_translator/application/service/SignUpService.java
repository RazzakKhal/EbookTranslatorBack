package com.ebook_translator.ebook_translator.application.service;

import com.ebook_translator.ebook_translator.configuration.property.KeycloakProperties;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.dto.request.SignUpRequest;
import com.ebook_translator.ebook_translator.infrastructure.adapter.in.web.dto.response.SignUpResponse;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignUpService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public SignUpService(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    public SignUpResponse signUp(SignUpRequest request) {

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(request.login());
        user.setEmail(request.login());
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());

        user.setCredentials(List.of(credential));

        Response response = keycloak.realm(keycloakProperties.getRealm())
                .users()
                .create(user);

        if (response.getStatus() == 201) {

            return new SignUpResponse("Inscription réussie");
        }

        if (response.getStatus() == 409) {
            throw new RuntimeException("Utilisateur déjà existant");
        }

        throw new RuntimeException("Erreur lors de l'inscription");
    }
}