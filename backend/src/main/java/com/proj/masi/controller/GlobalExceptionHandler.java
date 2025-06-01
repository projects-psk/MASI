package com.proj.masi.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class })
    public ResponseEntity<String> handleIntegrityViolation(Exception ex) {
        String msg = "Nie można usunąć definicji unitermu, ponieważ istnieją powiązane wyniki transformacji. " +
                "Usuń najpierw powiązane wyniki, a następnie spróbuj ponownie.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
    }
}
