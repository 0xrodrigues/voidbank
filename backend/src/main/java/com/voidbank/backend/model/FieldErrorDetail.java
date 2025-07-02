package com.voidbank.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalhes de erro de validação de campo")
public class FieldErrorDetail {

    @Schema(
            description = "Nome do campo com erro",
            example = "ownerName"
    )
    private String field;

    @Schema(
            description = "Mensagem de erro do campo",
            example = "Nome é obrigatório"
    )
    private String message;
}