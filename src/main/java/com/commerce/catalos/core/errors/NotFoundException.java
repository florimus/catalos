package com.commerce.catalos.core.errors;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }

}
