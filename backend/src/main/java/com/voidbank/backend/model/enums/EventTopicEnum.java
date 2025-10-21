package com.voidbank.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTopicEnum {

    TRANSACTION_PROCESS("voidbank.transaction.process.event", 1),
    TRANSACTION_FAILED_VALIDATION("voidbank.transaction.failed.validation.event", 2);

    private final String topic;
    private final Integer code;

}
