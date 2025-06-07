package com.voidbank.backend.controller.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateTransactionRequest {
    private Long from;
    private Long to;
    private BigDecimal amount;
    private String comments;
}
