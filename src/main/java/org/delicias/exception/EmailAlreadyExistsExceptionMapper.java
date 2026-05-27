package org.delicias.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EmailAlreadyExistsExceptionMapper implements ExceptionMapper<EmailAlreadyExistsException> {

    @Override
    public Response toResponse(EmailAlreadyExistsException exception) {
        UserErrorMapper error = new UserErrorMapper(
                exception.getMessage(),
                exception.getErrorCode()
        );
        return Response.status(exception.getStatus())
                .entity(error)
                .build();
    }
}
