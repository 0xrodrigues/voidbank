package com.voidbank.transaction_api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionProcessEventListener {

    private static final String TRANSACTION_PROCESS_TOPIC = "voidbank.transaction.process.event";

    @KafkaListener(topics = TRANSACTION_PROCESS_TOPIC)
    public void transactionListener(String event) {
        log.info("Event received from topic: {} - event: {}", TRANSACTION_PROCESS_TOPIC, event);
    }

}
