package com.voidbank.backend.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Resposta padrão da API com mensagem de sucesso")
public class MessageResponseApi {

    @Schema(
            description = "Mensagem de resposta da operação",
            example = "Operação realizada com sucesso"
    )
    private String message;

    @Schema(
            description = "Timestamp da resposta",
            example = "2024-01-15T10:30:00"
    )
    private LocalDateTime moment;
}
