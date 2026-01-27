package org.delicias.users.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.common.dto.UserZoneDTO;
import org.delicias.users.dto.CreateUserInfoReqDTO;
import org.delicias.users.service.UserInfoService;

import java.util.UUID;


@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserInfoService service;

    @POST
    @Authenticated
    public Response create(@Valid CreateUserInfoReqDTO request) {
        service.create(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }


    // TODO: Use in Rest Client
    @GET
    @Path("/{id}/zone")
    @Authenticated
    public UserZoneDTO getUserZone(@PathParam("id") UUID userId) {
        return service.getUserZone(userId);
    }

}
