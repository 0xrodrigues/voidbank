package com.voidbank.transaction_api.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {

    private Long nuTransaction;
    private String token;
    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal rate;
    private String comments;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
