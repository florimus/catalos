package com.commerce.catalos.core.errors;

import com.commerce.catalos.core.configurations.ErrorResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle {@link ConflictException} thrown by the application. This exception
     * represents
     * a conflict with the current state of the resource. It returns a 409 response
     * with a body
     * containing the error message.
     * 
     * @param ex the exception thrown
     * @return a response with a 409 status and the error message
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseEntity> handleConflictException(ConflictException ex) {
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(List.of(ex.getMessage())),
                HttpStatus.CONFLICT);
    }

    /**
     * Handle {@link UnAuthorizedException} thrown by the application. This
     * exception indicates
     * that the request requires user authentication. It returns a 401 response with
     * a body
     * containing the error message.
     * 
     * @param ex the exception thrown
     * @return a response with a 401 status and the error message
     */
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponseEntity> handleUnAuthorizedException(UnAuthorizedException ex) {
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(List.of(ex.getMessage())),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseEntity> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(List.of(ex.getMessage())),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle {@link NotFoundException} thrown by the application. This exception
     * indicates
     * that the requested resource is not found. It returns a 404 response with a
     * body
     * containing the error message.
     * 
     * @param ex the exception thrown
     * @return a response with a 404 status and the error message
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(List.of(ex.getMessage())),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Handle {@link MethodArgumentNotValidException} thrown by Spring when
     * validation on a
     * {@link org.springframework.web.bind.annotation.RequestBody} object fails.
     * This exception
     * is thrown by the application when a request body contains invalid data. It
     * returns a 400
     * response with a body containing all the error messages.
     * 
     * @param ex the exception thrown
     * @return a response with a 400 status and the error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseEntity> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        List<String> errors = new ArrayList<>();
        errors.add("You don't have permission to access this resource.");
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseEntity> handleUnknownException(Exception ex) {
        List<String> errors = new ArrayList<>();
        return new ResponseEntity<ErrorResponseEntity>(new ErrorResponseEntity(errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
