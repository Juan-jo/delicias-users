package org.delicias.exception;

public class UserErrorMapper {

    public String message;
    public String code;
    public long timestamp;

    public UserErrorMapper(String message, String code) {
        this.message = message;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }
}