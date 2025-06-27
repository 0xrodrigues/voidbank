package com.voidbank.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidbank.backend.model.enums.EventTopicEnum;
import com.voidbank.backend.model.Transaction;
import com.voidbank.backend.model.TransactionEvent;
import com.voidbank.backend.publisher.KafkaPublisher;
import com.voidbank.backend.repository.TransactionRepository;
import com.voidbank.backend.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private static final String TRANSACTION_PROCESS_TOPIC = EventTopicEnum.TRANSACTION_PROCESS.getTopic();

    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;
    private final KafkaPublisher publisher;
    private final ObjectMapper objectMapper;

    public void transferFunds(Transaction transaction) {
        log.info("transferring funds and creating transaction - {}", transaction);
        transactionValidator.validate(transaction);
        transaction.fill();
        transactionRepository.insert(transaction);

        try {
            String eventAsString = objectMapper.writeValueAsString(new TransactionEvent(transaction));
            publisher.send(TRANSACTION_PROCESS_TOPIC, eventAsString);
        } catch (Exception ex) {
            log.error("Error when producer event to {} topic", TRANSACTION_PROCESS_TOPIC, ex);
        }
    }

}
