package com.voidbank.transaction_api.transaction.controller.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateTransactionRequest {
    private String from;
    private String to;
    private BigDecimal amount;
    private String comments;
}
