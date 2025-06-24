package com.voidbank.backend.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidbank.backend.exceptions.AccountNotFoundException;
import com.voidbank.backend.exceptions.InsufficientBalanceException;
import com.voidbank.backend.publisher.KafkaPublisher;
import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.model.TransactionFailedValidationEvent;
import com.voidbank.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionValidator {

    private final AccountService accountService;
    private final KafkaPublisher kafkaPublisher;
    private final ObjectMapper objectMapper;

    private static final String TRANSACTION_FAILED_VALIDATION_TOPIC = "voidbank.transaction.failed.validation.event";

    public void validate(Transaction transaction) {
        log.info("Starting account validation - from {} - to {}", transaction.getFrom(), transaction.getTo());
        try {
            validateAmount(transaction);
            validateAccounts(transaction);
            validateSufficientBalance(transaction);
        } catch (Exception ex) {
            log.error("Transaction failed validation - {}", transaction);

            try {
                String exMessage = ex.getMessage();
                String eventAsString = objectMapper.writeValueAsString(
                        new TransactionFailedValidationEvent(transaction.getFrom(), transaction.getTo(), exMessage));
                kafkaPublisher.send(TRANSACTION_FAILED_VALIDATION_TOPIC, eventAsString);
            } catch (JsonProcessingException jsonProcessingException) {
                log.error("Error when producer event to {} topic", TRANSACTION_FAILED_VALIDATION_TOPIC, ex);
            }

            throw ex;
        }
    }

    private void validateAmount(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private void validateAccounts(Transaction transaction) {
        Long from = transaction.getFrom();
        Long to = transaction.getTo();

        if (from == null || to == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' accounts must be provided.");
        }

        if (from.equals(to)) {
            throw new IllegalArgumentException("'from' and 'to' accounts must be different.");
        }

        if (!accountService.accountExists(from)) {
            throw new AccountNotFoundException("Origin account does not exist: " + from);
        }

        if (!accountService.accountExists(to)) {
            throw new AccountNotFoundException("Destination account does not exist: " + to);
        }
    }

    private void validateSufficientBalance(Transaction transaction) {
        BigDecimal balance = accountService.getBalance(transaction.getFrom());
        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in the origin account.");
        }
    }
}
