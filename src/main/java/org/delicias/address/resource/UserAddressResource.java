package org.delicias.address.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.delicias.address.dto.CreateUserAddressReqDTO;
import org.delicias.address.service.UserAddressService;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

@Authenticated
@Path("/api/users/addresses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserAddressResource {

    @Inject
    UserAddressService service;

    @GET
    public Response userAddresses() {

        var addresses = service.listUserAddress();

        return Response.ok(addresses).build();
    }

    @POST
    public Response create(
            @Valid @ConvertGroup(to = OnCreate.class) CreateUserAddressReqDTO request) {

        service.create(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response update(
            @Valid @ConvertGroup(to = OnUpdate.class) CreateUserAddressReqDTO request) {

        service.update(request);
        return Response.status(Response.Status.OK).build();

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {

        service.delete(id);
        return Response.noContent().build();
    }

    // TODO: Use in Rest Client
    @GET
    @Path("/default")
    public Response getDefaultAddress() {

        return Response.ok(
                service.getDefaultAddress()
        ).build();
    }

    // TODO: Use in Rest Client
    @GET
    @Path("/{addressId}/shopping")
    public Response getShoppingAddress(
            @PathParam("addressId") Integer addressId
    ) {
        return Response.ok(
                service.getShoppingAddressDTO(addressId)
        ).build();
    }

}
