package com.voidbank.transaction_api.service;

import com.voidbank.transaction_api.validator.TransactionValidator;
import com.voidbank.transaction_api.model.Transaction;
import com.voidbank.transaction_api.model.TransactionStatus;
import com.voidbank.transaction_api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    public void transferFunds(Transaction transaction) {
        log.info("transferring funds and creating transaction - {}", transaction);
        transactionValidator.validate(transaction);
        fill(transaction);
        transactionRepository.insert(transaction);
    }

    private void fill(Transaction transaction) {
        transaction.setToken(UUID.randomUUID().toString());
        transaction.setRate(new BigDecimal("1.5"));
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
    }

}
