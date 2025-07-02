package com.voidbank.backend.controller.request;

import com.voidbank.backend.model.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Dados necessários para criação de uma nova conta bancária")
public class CreateAccountRequest {

    @Schema(
            description = "Nome completo do titular da conta",
            example = "João Silva Santos",
            required = true
    )
    private String ownerName;

    @Schema(
            description = "Número do documento (CPF ou CNPJ)",
            example = "12345678901",
            required = true
    )
    private String document;

    @Schema(
            description = "Tipo do documento",
            example = "CPF",
            required = true,
            allowableValues = {"CPF", "CNPJ"}
    )
    private DocumentType documentType;

}
