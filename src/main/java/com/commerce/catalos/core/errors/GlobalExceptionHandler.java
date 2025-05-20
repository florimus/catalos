package com.commerce.catalos.core.errors;

import com.commerce.catalos.core.configurations.ErrorResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseEntity> handleConflictException(ConflictException ex) {
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(List.of(ex.getMessage())),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponseEntity> handleUnAuthorizedException(ConflictException ex) {
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(List.of(ex.getMessage())),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundException(ConflictException ex) {
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(List.of(ex.getMessage())),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(errors), HttpStatus.BAD_REQUEST);
    }
}
