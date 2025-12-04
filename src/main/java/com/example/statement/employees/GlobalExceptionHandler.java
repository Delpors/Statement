package com.example.statement.employees;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler (Exception.class)
    public ResponseEntity <String> handleGenericException (
            Exception e
    ){
        log.error("Handle exception ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    @ExceptionHandler (exception= {
            EntityNotFoundException.class,
            NoSuchElementException.class
    })
    public ResponseEntity <String> handleNotFoundException (
            EntityNotFoundException e
    ){
        log.error("Handle EntityNotFoundException ", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity <String> handleIllegalException (
            IllegalArgumentException e
    ){
        log.error("Handle IllegalArgumentException ", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
