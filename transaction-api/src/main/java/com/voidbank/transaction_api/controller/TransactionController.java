package com.voidbank.transaction_api.controller;

import com.voidbank.transaction_api.controller.request.CreateTransactionRequest;
import com.voidbank.transaction_api.controller.response.MessageResponseApi;
import com.voidbank.transaction_api.model.Transaction;
import com.voidbank.transaction_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.voidbank.transaction_api.util.MapperUtil.map;

@RestController
@RequestMapping("/api/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<MessageResponseApi> transferFunds(@RequestBody CreateTransactionRequest request) {
        transactionService.transferFunds(map(request, Transaction.class));
        return ResponseEntity.ok(new MessageResponseApi("Transação realizada com sucesso", LocalDateTime.now()));
    }

}
