package com.voidbank.backend.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTopicEnum {

    TRANSACTION_PROCESS("voidbank.transaction.process.event", 1);

    private final String topic;
    private final Integer code;

}
