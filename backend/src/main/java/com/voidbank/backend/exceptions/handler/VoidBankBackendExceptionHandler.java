package com.voidbank.backend.exceptions.handler;

import com.voidbank.backend.exceptions.AccountNotFoundException;
import com.voidbank.backend.exceptions.CustomExceptionObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class VoidBankBackendExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<CustomExceptionObject> handleAccountNotFoundException(AccountNotFoundException ex,
                                                                                HttpServletRequest request) {
        CustomExceptionObject response = new CustomExceptionObject(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
