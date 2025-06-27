package com.voidbank.backend.model;

import com.voidbank.backend.model.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {

    private Long nuTransaction;
    private String token;
    private Long from;
    private Long to;
    private BigDecimal amount;
    private BigDecimal rate;
    private String comments;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void fill() {
        this.token = UUID.randomUUID().toString();
        this.rate = new BigDecimal("1.5");
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
