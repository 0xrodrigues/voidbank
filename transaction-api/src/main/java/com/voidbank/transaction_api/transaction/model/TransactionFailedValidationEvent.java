package com.voidbank.transaction_api.transaction.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionFailedValidationEvent {

    private String from;
    private String to;
    private String reason;
    private LocalDateTime moment;

    public TransactionFailedValidationEvent(String from, String to, String reason) {
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.moment = LocalDateTime.now();
    }

}
