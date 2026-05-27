package org.delicias.exception;

import lombok.Getter;

public class EmailAlreadyExistsException extends RuntimeException {

    @Getter
    private final int status;

    public EmailAlreadyExistsException(String message, int status) {
        super(message);
        this.status = status;
    }

    public String getErrorCode() {
        return "USER_EMAIL_ALREADY_EXISTS";
    }
}
