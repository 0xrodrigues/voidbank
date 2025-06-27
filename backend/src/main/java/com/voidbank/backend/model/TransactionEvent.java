package com.voidbank.backend.model;

import com.voidbank.backend.model.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionEvent {

    private String correlationId;
    private Long from;
    private Long to;
    private BigDecimal amount;
    private BigDecimal rate;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public TransactionEvent(Transaction transaction) {
        this.correlationId = transaction.getToken();
        this.from = transaction.getFrom();
        this.to = transaction.getTo();
        this.amount = transaction.getAmount();
        this.rate = transaction.getRate();
        this.status = transaction.getStatus();
        this.createdAt = transaction.getCreatedAt();
    }

}
