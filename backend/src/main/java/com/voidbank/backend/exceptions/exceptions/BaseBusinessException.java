package com.voidbank.backend.exceptions.exceptions;

import com.voidbank.backend.model.ErrorResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public abstract class BaseBusinessException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(BaseBusinessException.class);

    private final ErrorResponse errorResponse;

    public BaseBusinessException(BusinessExceptionIndicator indicator, String errorId) {
        super(indicator.getErrorMessage());
        this.errorResponse = indicator.getAsErrorResponse(errorId);

        logError();
    }

    private void logError() {
        StackTraceElement caller = findCaller();
        log.warn("""
                        BusinessException lançada:
                        → errorId: {}
                        → indicador: {}
                        → mensagem: {}
                        → origem: {}:{} ({})
                        """,
                errorResponse.getErrorId(),
                errorResponse.getError(),
                errorResponse.getErrorMessage(),
                caller.getClassName(),
                caller.getLineNumber(),
                caller.getMethodName()
        );
    }

    private StackTraceElement findCaller() {
        for (StackTraceElement element : getStackTrace()) {
            if (!element.getClassName().contains("BaseBusinessException")) {
                return element;
            }
        }
        return getStackTrace()[0];
    }
}
