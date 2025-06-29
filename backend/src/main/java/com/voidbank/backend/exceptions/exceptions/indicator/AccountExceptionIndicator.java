package com.voidbank.backend.exceptions.exceptions.indicator;

import com.voidbank.backend.exceptions.exceptions.BusinessExceptionIndicator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public enum AccountExceptionIndicator implements BusinessExceptionIndicator {

    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "Account with ID %s not found", HttpStatus.NOT_FOUND),
    ACCOUNT_INSUFFICIENT_BALANCE("ACCOUNT_INSUFFICIENT_BALANCE", "Insufficient balance in the origin account", HttpStatus.BAD_REQUEST),
    ACCOUNT_WITH_DOCUMENT_ALREADY_EXISTS("ACCOUNT_WITH_DOCUMENT_ALREADY_EXISTS", "Unable to create account, document already registered", HttpStatus.BAD_REQUEST),
    RETRIEVING_BALANCE("RETRIEVING_BALANCE", "Unable to retrieve balance", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String error;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public LocalDateTime getMoment() {
        return LocalDateTime.now();
    }
}
