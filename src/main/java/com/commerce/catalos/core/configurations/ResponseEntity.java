package com.commerce.catalos.core.configurations;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ResponseEntity<T> {

    private T data;

    private boolean success;
}
