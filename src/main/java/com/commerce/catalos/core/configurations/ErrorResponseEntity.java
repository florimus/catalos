package com.commerce.catalos.core.configurations;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponseEntity {

    private Object data;

    private boolean success;

    private List<String> message;

    public ErrorResponseEntity(List<String> message){
        this.data = null;
        this.success = false;
        this.message = message;
    }
}
