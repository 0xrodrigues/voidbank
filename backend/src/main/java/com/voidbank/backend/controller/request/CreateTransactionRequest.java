package com.voidbank.backend.controller.request;

import com.voidbank.backend.model.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Dados necessários para realizar uma transação entre contas")
public class CreateTransactionRequest {

    @Schema(
            description = "Número da conta de origem",
            example = "12345",
            required = true
    )
    private Long from;

    @Schema(
            description = "Número da conta de destino",
            example = "67890",
            required = true
    )
    private Long to;

    @Schema(
            description = "Valor da transação",
            example = "100.50",
            required = true
    )
    private BigDecimal amount;

    @Schema(
            description = "Comentários ou observações sobre a transação",
            example = "Pagamento de serviços"
    )
    private String comments;

    @Schema(
            description = "Tipo da transação",
            example = "PIX",
            required = true
    )
    private TransactionType type;
}
