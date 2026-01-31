package com.tuproyecto.inventory.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarError400(MethodArgumentNotValidException e) {
        Map<String, String> errores = new HashMap<>();

        e.getFieldErrors().forEach(f -> {
            errores.put(f.getField(), f.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404() {
        return ResponseEntity.notFound().build();
    }
}