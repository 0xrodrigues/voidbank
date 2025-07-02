package com.voidbank.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta de erro da API")
public class ErrorResponse {

    @Schema(
            description = "Identificador único do erro",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private String errorId;

    @Schema(
            description = "Código do erro",
            example = "ACCOUNT_NOT_FOUND"
    )
    private String error;

    @Schema(
            description = "Mensagem descritiva do erro",
            example = "Conta não encontrada"
    )
    private String errorMessage;

    @Schema(
            description = "Timestamp do erro",
            example = "2024-01-15T10:30:00"
    )
    private LocalDateTime moment;

    @Schema(
            description = "Código de status HTTP",
            example = "404"
    )
    private int httpStatus;

    @Schema(
            description = "Lista de erros de validação de campos"
    )
    private List<FieldErrorDetail> fieldErrors;
}
