package com.voidbank.backend.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionFailedValidationEvent {

    private Long from;
    private Long to;
    private String reason;
    private LocalDateTime moment;

    public TransactionFailedValidationEvent(Long from, Long to, String reason) {
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.moment = LocalDateTime.now();
    }

}
