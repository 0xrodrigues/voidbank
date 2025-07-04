package com.voidbank.backend.controller;

import com.voidbank.backend.api.TransactionApi;
import com.voidbank.backend.controller.request.CreateTransactionRequest;
import com.voidbank.backend.controller.response.MessageResponseApi;
import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.voidbank.backend.util.MapperUtil.map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<MessageResponseApi> transferFunds(CreateTransactionRequest request) {
        transactionService.transferFunds(map(request, Transaction.class));
        return ResponseEntity.ok(new MessageResponseApi("Transaction completed successfully", LocalDateTime.now()));
    }

}
