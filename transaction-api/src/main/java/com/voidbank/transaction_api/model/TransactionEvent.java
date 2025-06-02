package com.voidbank.transaction_api.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionEvent {

    private String token;
    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal rate;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public TransactionEvent(Transaction transaction) {
        this.token = transaction.getToken();
        this.from = transaction.getFrom();
        this.to = transaction.getTo();
        this.amount = transaction.getAmount();
        this.rate = transaction.getRate();
        this.status = transaction.getStatus();
        this.createdAt = transaction.getCreatedAt();
    }

}
