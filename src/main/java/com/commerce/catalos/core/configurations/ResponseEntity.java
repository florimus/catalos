package com.commerce.catalos.core.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity<T> {

    private T data;

    private boolean success;

    private String message;

    private List<String> traces;

    //  For success responses
    public ResponseEntity(T data){
        this.data = data;
        this.success = true;
        this.message = null;
        this.traces = new ArrayList<>();
    }

    //  For error responses
    public ResponseEntity(String message, List<String> traces){
        this.data = null;
        this.success = false;
        this.message = message;
        this.traces = traces;
    }
}
