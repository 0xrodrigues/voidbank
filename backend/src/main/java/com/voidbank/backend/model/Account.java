package com.voidbank.backend.model;

import com.voidbank.backend.model.enums.DocumentType;
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
    private Integer digit;
    private Integer agency;
    private String ownerName;
    private String document;
    private BigDecimal balance;
    private DocumentType documentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
