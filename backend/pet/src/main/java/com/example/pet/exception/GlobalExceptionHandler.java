package com.example.pet.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex)
    {
        List<String>errorList = ex.getBindingResult()
            .getFieldErrors().stream().map(e-> e.getDefaultMessage())
            .collect(Collectors.toList());

            Map<String,Object> response = new HashMap<>();
            response.put("message","Validation failed");
            response.put("errors",errorList);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
}
