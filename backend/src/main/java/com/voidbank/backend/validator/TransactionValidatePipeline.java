package com.voidbank.backend.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.model.TransactionFailedValidationEvent;
import com.voidbank.backend.model.enums.EventTopicEnum;
import com.voidbank.backend.publisher.KafkaPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionValidatePipeline {

    private static final String TRANSACTION_FAILED_VALIDATION_TOPIC = EventTopicEnum.TRANSACTION_FAILED_VALIDATION.getTopic();

    private final ObjectMapper objectMapper;
    private final List<TransactionValidator> validators;
    private final KafkaPublisher publisher;

    public void validate(Transaction transaction) {
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
