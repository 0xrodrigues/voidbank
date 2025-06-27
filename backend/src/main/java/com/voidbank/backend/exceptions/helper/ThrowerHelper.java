package com.voidbank.backend.exceptions.helper;

import com.voidbank.backend.exceptions.builder.ExceptionBuilder;
import com.voidbank.backend.exceptions.exceptions.BusinessExceptionIndicator;

public class ThrowerHelper {
    public static void throwException(BusinessExceptionIndicator exception) {
        throw ExceptionBuilder.withIndicator(exception).build();
    }
}
