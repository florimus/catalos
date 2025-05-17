package com.commerce.catalos.core.errors;

import java.util.List;

public class ConflictException extends RuntimeException {

    public ConflictException(final String message){
        super(message);
    }

}
