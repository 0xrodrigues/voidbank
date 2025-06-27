package com.voidbank.backend.exceptions.exceptions;

import com.voidbank.backend.model.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public interface BusinessExceptionIndicator {
    String getError();

    String getErrorMessage();

    LocalDateTime getMoment();

    HttpStatus getHttpStatus();

    default ErrorResponse getAsErrorResponse(String errorId) {
        return new ErrorResponse(
                errorId,
                getError(),
                getErrorMessage(),
                LocalDateTime.now(),
                getHttpStatus().value(),
                null
        );
    }
}
