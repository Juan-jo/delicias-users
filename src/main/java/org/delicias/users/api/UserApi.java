package org.delicias.users.api;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.users.dto.CreateUserInfoReqDTO;
import org.delicias.users.service.UserInfoService;

import java.util.UUID;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserApi {

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


}
