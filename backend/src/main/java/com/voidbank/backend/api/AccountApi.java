package com.voidbank.backend.api;

import com.voidbank.backend.controller.request.CreateAccountRequest;
import com.voidbank.backend.controller.response.AccountResponse;
import com.voidbank.backend.controller.response.MessageResponseApi;
import com.voidbank.backend.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Accounts", description = "Operações relacionadas ao gerenciamento de contas bancárias")
@RequestMapping("/api/account")
public interface AccountApi {

    @Operation(
            summary = "Criar nova conta bancária",
            description = "Cria uma nova conta bancária no sistema VoidBank com os dados fornecidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conta criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponseApi.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou conta já existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    ResponseEntity<MessageResponseApi> createAccount(@RequestBody CreateAccountRequest req);

    @Operation(
            summary = "Buscar conta bancária por ID",
            description = "Retorna os dados completos de uma conta bancária específica usando o número da conta"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conta encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Conta não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{accountId}")
    ResponseEntity<AccountResponse> getAccountById(
            @Parameter(
                    description = "Número da conta bancária",
                    example = "12345",
                    required = true
            )
            @PathVariable Long accountId
    );
}
