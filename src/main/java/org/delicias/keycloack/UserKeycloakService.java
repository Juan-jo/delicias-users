package org.delicias.keycloack;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.delicias.SecurityContextService;
import org.delicias.mobile.dto.UpdateUserInfoReqDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;


@ApplicationScoped
public class UserKeycloakService {

    @Inject
    Keycloak keycloak;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.realm")
    String realm;

    @Inject
    SecurityContextService security;

    public void updateUserName(UpdateUserInfoReqDTO req) {

        String userId = security.userId();

        UserRepresentation user = keycloak.realm(realm)
                .users()
                .get(userId)
                .toRepresentation();

        if (user == null) {
            throw new NotFoundException("Usuario no encontrado en Keycloak");
        }


        user.setFirstName(req.name());
        user.setLastName(req.lastName());
        user.setEmail(req.email());

        try {
            // 3. Intentar la actualización
            keycloak.realm(realm).users().get(userId).update(user);
        } catch (WebApplicationException e) {
            Response response = e.getResponse();

            // Keycloak devuelve 409 Conflict si el email ya existe
            if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {

                throw new WebApplicationException("El email ya existe", Response.Status.CONFLICT);
            }

            // Otros errores (ej. formato de email inválido si Keycloak lo valida)
            if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                throw new BadRequestException("Datos inválidos para la actualización en Keycloak.");
            }

            throw e; // Relanzar si es un error desconocido
        }
    }
}
