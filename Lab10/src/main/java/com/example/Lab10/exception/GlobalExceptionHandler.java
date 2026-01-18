package com.example.Lab10.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleSecurityExceptions(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        if (ex.getMessage().contains("403") || ex.getMessage().contains("Access Denied")) {
            errorResponse.put("error", "Security Violation");
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}