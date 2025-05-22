package com.commerce.catalos.core.errors;

public class BadRequestException extends RuntimeException {

    public BadRequestException(final String message) {
        super(message);
    }

}
