package com.voidbank.backend.exceptions.builder;


import com.voidbank.backend.exceptions.exceptions.BaseBusinessException;
import com.voidbank.backend.exceptions.exceptions.BusinessExceptionIndicator;

import java.util.UUID;

public class ExceptionBuilder {

    private BusinessExceptionIndicator indicator;
    private String errorId;

    private ExceptionBuilder() {
        this.errorId = UUID.randomUUID().toString(); // gera ID único automaticamente
    }

    public static ExceptionBuilder withIndicator(BusinessExceptionIndicator indicator) {
        ExceptionBuilder builder = new ExceptionBuilder();
        builder.indicator = indicator;
        return builder;
    }

    public ExceptionBuilder errorId(String errorId) {
        this.errorId = errorId;
        return this;
    }

    public BaseBusinessException build() {
        return new BaseBusinessException(indicator, errorId) {};
    }
}
