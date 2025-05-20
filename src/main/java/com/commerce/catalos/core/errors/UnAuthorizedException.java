package com.commerce.catalos.core.errors;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(final String message) {
        super(message);
    }

}
