package com.voidbank.backend.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {

    private Long nuAccount;
    private Long digit;
    private Long agency;
    private String ownerName;
    private String document;
    private BigDecimal balance;
    private DocumentType documentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
