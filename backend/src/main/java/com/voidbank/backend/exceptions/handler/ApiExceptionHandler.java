package com.voidbank.backend.exceptions.handler;

import com.voidbank.backend.exceptions.exceptions.BaseBusinessException;
import com.voidbank.backend.exceptions.exceptions.indicator.CommonExceptionIndicator;
import com.voidbank.backend.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<ErrorResponse> handleBaseBusinessException(BaseBusinessException ex) {
        ErrorResponse error = ex.getErrorResponse();
        log.warn("Handled BaseBusinessException: {} | errorId={}", error.getError(), error.getErrorId());
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getHttpStatus()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        String errorId = UUID.randomUUID().toString();
        ErrorResponse response = CommonExceptionIndicator.NULL_POINTER.getAsErrorResponse(errorId);
        log.warn("NullPointerException | errorId={}", errorId, ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Throwable ex) {
        String errorId = UUID.randomUUID().toString();
        ErrorResponse response = CommonExceptionIndicator.INTERNAL_ERROR.getAsErrorResponse(errorId);
        log.warn("Unhandled exception | errorId={}", errorId, ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
