package com.voidbank.backend.controller.response;

import com.voidbank.backend.model.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Dados completos da conta bancária")
public class AccountResponse {

    @Schema(
            description = "Número da conta",
            example = "12345"
    )
    private Long nuAccount;

    @Schema(
            description = "Dígito verificador da conta",
            example = "7"
    )
    private Integer digit;

    @Schema(
            description = "Número da agência",
            example = "100"
    )
    private Integer agency;

    @Schema(
            description = "Nome completo do titular da conta",
            example = "João Silva Santos"
    )
    private String ownerName;

    @Schema(
            description = "Documento do titular (CPF/RG)",
            example = "12345678901"
    )
    private String document;

    @Schema(
            description = "Saldo atual da conta",
            example = "1500.75"
    )
    private BigDecimal balance;

    @Schema(
            description = "Tipo do documento",
            example = "CPF"
    )
    private DocumentType documentType;

    @Schema(
            description = "Data de criação da conta",
            example = "2024-01-15T10:30:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Data da última atualização",
            example = "2024-01-20T14:45:30"
    )
    private LocalDateTime updatedAt;
}
