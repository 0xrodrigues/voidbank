package com.voidbank.backend.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.voidbank.backend.EventTopicEnum;
import com.voidbank.backend.model.TransactionEvent;
import com.voidbank.backend.model.TransactionStatus;
import com.voidbank.backend.repository.AccountRepository;
import com.voidbank.backend.repository.TransactionRepository;
import com.voidbank.backend.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionProcessEventListener {

    private static final String TRANSACTION_PROCESS_TOPIC = EventTopicEnum.TRANSACTION_PROCESS.getTopic();

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @KafkaListener(topics = "${transaction.process.topic}")
    public void transactionListener(String event) {
        log.info("Event received from topic: {} - event: {}", TRANSACTION_PROCESS_TOPIC, event);

        try {
            TransactionEvent transactionEvent = ObjectMapperUtil.getMapper().readValue(event, TransactionEvent.class);
            accountRepository.updateBalances(transactionEvent);
            transactionRepository.updateStatus(transactionEvent.getCorrelationId(), TransactionStatus.PROCESSED);

            log.info("Balances updated successfully for transaction - {}", transactionEvent.getCorrelationId());
        } catch (JsonProcessingException ex) {
            log.error("Error converting JSON event {}", event, ex);
        }
    }

}
