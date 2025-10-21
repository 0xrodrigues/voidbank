package com.voidbank.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.model.TransactionEvent;
import com.voidbank.backend.model.TransactionFailedValidationEvent;
import com.voidbank.backend.model.enums.EventTopicEnum;
import com.voidbank.backend.publisher.KafkaPublisher;
import com.voidbank.backend.repository.TransactionRepository;
import com.voidbank.backend.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private static final String TRANSACTION_PROCESS_TOPIC = EventTopicEnum.TRANSACTION_PROCESS.getTopic();
    private static final String TRANSACTION_FAILED_VALIDATION_TOPIC = EventTopicEnum.TRANSACTION_FAILED_VALIDATION.getTopic();

    private final TransactionRepository transactionRepository;
    private final KafkaPublisher publisher;
    private final ObjectMapper objectMapper;
    private final List<TransactionValidator> validators;

    public void transferFunds(Transaction transaction) {
        log.info("transferring funds and creating transaction - {}", transaction);
        validateTransaction(transaction);
        transaction.fill();
        transactionRepository.insert(transaction);

        try {
            String eventAsString = objectMapper.writeValueAsString(new TransactionEvent(transaction));
            publisher.send(TRANSACTION_PROCESS_TOPIC, eventAsString);
        } catch (Exception ex) {
            log.error("Error when producer event to {} topic", TRANSACTION_PROCESS_TOPIC, ex);
        }
    }

    private void validateTransaction(Transaction transaction) {
        try {
            validators.forEach(v -> v.validate(transaction));
        } catch (Exception ex) {
            log.error("Transaction validation failed - {}", transaction);

            try {
                log.info("Producer transaction validation failed event to kafka - {}", transaction);
                String exMessage = ex.getMessage();
                String eventAsString = objectMapper.writeValueAsString(
                        new TransactionFailedValidationEvent(transaction.getFrom(), transaction.getTo(), exMessage));
                publisher.send(TRANSACTION_FAILED_VALIDATION_TOPIC, eventAsString);
            } catch (JsonProcessingException jsonProcessingException) {
                log.error("Error when producer event to {} topic", TRANSACTION_FAILED_VALIDATION_TOPIC, ex);
            }
        }
    }

}
