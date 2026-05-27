package org.delicias.mobile.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.address.service.UserAddressService;
import org.delicias.keycloack.UserKeycloakService;
import org.delicias.mobile.dto.CreateUserInfoReqDTO;
import org.delicias.mobile.dto.UpdateUserInfoReqDTO;
import org.delicias.mobile.service.UserDefaultAddressService;
import org.delicias.mobile.service.UserRegisterService;
import org.delicias.users.service.UserInfoService;

import java.util.Map;

@Authenticated
@Path("/api/users/mobile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserInfoResource {

    @Inject
    UserRegisterService registerService;

    @Inject
    UserAddressService addressService;

    @Inject
    UserDefaultAddressService userDefaultAddressService;

    @Inject
    UserInfoService userInfoService;

    @GET
    @Path("/me")
    public Response getFields(
            @QueryParam("fields") @DefaultValue("") String fields
    ) {
        return Response.ok(
                userInfoService.meWithFields(fields)
        ).build();
    }

    @PATCH
    @Path("/me")
    public Response patchFields(
            Map<String, Object> payload
    ) {
        userInfoService.pathInfo(payload);
        return Response.noContent().build();
    }

    @POST
    public Response create(@Valid CreateUserInfoReqDTO request) {
        registerService.registerUser(request);
        return Response.status(Response.Status.CREATED).build();
    }


    @PUT
    public Response updateInfo(
            @Valid UpdateUserInfoReqDTO req
    ) {

        registerService.updateInfo(req);

        return Response.ok().build();
    }

    @GET
    @Path("/address/default")
    public Response addressDefault() {

        return Response.ok(
                userDefaultAddressService.getDefaultAddress()
        ).build();

    }

    @GET
    @Path("/address/confirm")
    public Response confirmAddress(
            @QueryParam("latitude") double latitude,
            @QueryParam("longitude") double longitude
    ) {

        return Response.ok(
                addressService.confirmAddress(latitude, longitude)
        ).build();

    }

}
