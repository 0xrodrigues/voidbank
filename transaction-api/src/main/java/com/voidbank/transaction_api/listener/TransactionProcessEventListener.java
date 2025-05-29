package com.voidbank.transaction_api.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voidbank.transaction_api.transaction.model.TransactionEvent;
import com.voidbank.transaction_api.transaction.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionProcessEventListener {

    private static final String TRANSACTION_PROCESS_TOPIC = "voidbank.transaction.process.event";

    private final AccountRepository accountRepository;

    @KafkaListener(topics = TRANSACTION_PROCESS_TOPIC)
    public void transactionListener(String event) {
        log.info("Event received from topic: {} - event: {}", TRANSACTION_PROCESS_TOPIC, event);

        try {
            TransactionEvent transactionEvent = new ObjectMapper().readValue(event, TransactionEvent.class);
            accountRepository.updateBalances(transactionEvent);

            log.info("Balances updated successfully for transaction - {}", transactionEvent.getToken());
        } catch (JsonProcessingException ex) {
            log.error("Error converting JSON event {}", event, ex);
        }

    }
}
