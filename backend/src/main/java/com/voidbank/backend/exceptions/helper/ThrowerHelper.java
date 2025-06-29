package com.voidbank.backend.exceptions.helper;

import com.voidbank.backend.exceptions.builder.ExceptionBuilder;
import com.voidbank.backend.exceptions.exceptions.BusinessExceptionIndicator;

public class ThrowerHelper {
    private ThrowerHelper() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static void throwException(BusinessExceptionIndicator exception) {
        throw ExceptionBuilder.withIndicator(exception).build();
    }
}
