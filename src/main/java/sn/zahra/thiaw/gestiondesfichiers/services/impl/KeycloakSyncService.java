package sn.zahra.thiaw.gestiondesfichiers.services.impl;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sn.zahra.thiaw.gestiondesfichiers.datas.entities.UserEntity;
import sn.zahra.thiaw.gestiondesfichiers.web.dtos.requests.RegisterUserDTO;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakSyncService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.admin-username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl.replace("/realms/master", ""))
                .realm("master")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    public void createKeycloakUser(RegisterUserDTO userDTO, UserEntity.Role role) {
        try {
            Keycloak keycloak = getKeycloakInstance();

            // Vérifier si l'utilisateur existe déjà
            if (!keycloak.realm(realm).users().search(userDTO.getEmail()).isEmpty()) {
                throw new RuntimeException("Un utilisateur avec cet email existe déjà dans Keycloak");
            }

            // Créer les credentials
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userDTO.getPassword());
            credential.setTemporary(false);

            // Créer la représentation de l'utilisateur
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getEmail());
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getPrenom());
            user.setLastName(userDTO.getNom());
            user.setEnabled(true);
            user.setCredentials(Collections.singletonList(credential));
            user.setEmailVerified(true);

            // Créer l'utilisateur dans Keycloak
            Response response = keycloak.realm(realm).users().create(user);

            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                throw new RuntimeException("Échec de la création de l'utilisateur dans Keycloak. Status: " + response.getStatus());
            }

            // Récupérer l'ID de l'utilisateur créé
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            response.close();

            // Récupérer le rôle correspondant dans Keycloak
            RoleRepresentation userRole = keycloak.realm(realm).roles()
                    .get(role.name().toLowerCase())
                    .toRepresentation();

            // Assigner le rôle à l'utilisateur
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            userResource.roles().realmLevel().add(List.of(userRole));

            System.out.println("Utilisateur créé avec succès dans Keycloak avec le rôle '" + role.name() + "': " + userDTO.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de l'utilisateur dans Keycloak: " + e.getMessage(), e);
        }
    }
}