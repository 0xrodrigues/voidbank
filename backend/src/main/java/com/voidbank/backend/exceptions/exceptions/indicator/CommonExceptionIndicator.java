package com.voidbank.backend.exceptions.exceptions.indicator;

import com.voidbank.backend.exceptions.exceptions.BusinessExceptionIndicator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public enum CommonExceptionIndicator implements BusinessExceptionIndicator {

    INVALID_FIELDS("INVALID_FIELDS", "Campos inválidos na requisição", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("INTERNAL_ERROR", "Erro interno inesperado", HttpStatus.INTERNAL_SERVER_ERROR),
    NULL_POINTER("NULL_POINTER", "Erro inesperado (null pointer)", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String error;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public LocalDateTime getMoment() {
        return LocalDateTime.now();
    }
}
