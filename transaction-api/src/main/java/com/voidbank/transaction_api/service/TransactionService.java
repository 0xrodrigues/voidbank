package com.voidbank.transaction_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidbank.transaction_api.publisher.KafkaPublisher;
import com.voidbank.transaction_api.model.Transaction;
import com.voidbank.transaction_api.model.TransactionEvent;
import com.voidbank.transaction_api.model.TransactionStatus;
import com.voidbank.transaction_api.repository.TransactionRepository;
import com.voidbank.transaction_api.validator.TransactionValidator;
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

    private static final String TRANSACTION_PROCESS_TOPIC = "voidbank.transaction.process.event";

    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;
    private final KafkaPublisher publisher;
    private final ObjectMapper objectMapper;

    public void transferFunds(Transaction transaction) {
        log.info("transferring funds and creating transaction - {}", transaction);
        transactionValidator.validate(transaction);
        fill(transaction);
        transactionRepository.insert(transaction);

        try {
            String eventAsString = objectMapper.writeValueAsString(new TransactionEvent(transaction));
            publisher.send(TRANSACTION_PROCESS_TOPIC, eventAsString);
        } catch (Exception ex) {
            log.error("Error when producer event to {} topic", TRANSACTION_PROCESS_TOPIC, ex);
        }
    }

    private void fill(Transaction transaction) {
        transaction.setToken(UUID.randomUUID().toString());
        transaction.setRate(new BigDecimal("1.5"));
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
    }

}
