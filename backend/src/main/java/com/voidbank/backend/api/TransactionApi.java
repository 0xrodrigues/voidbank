package com.voidbank.backend.api;

import com.voidbank.backend.controller.request.CreateTransactionRequest;
import com.voidbank.backend.controller.response.MessageResponseApi;
import com.voidbank.backend.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Transactions", description = "Operações relacionadas a transações financeiras")
@RequestMapping("/api/transaction")
public interface TransactionApi {

    @Operation(
            summary = "Realizar transferência entre contas",
            description = "Executa uma transferência de fundos entre duas contas bancárias"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseApi.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou saldo insuficiente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    ResponseEntity<MessageResponseApi> transferFunds(@RequestBody CreateTransactionRequest request);
}
