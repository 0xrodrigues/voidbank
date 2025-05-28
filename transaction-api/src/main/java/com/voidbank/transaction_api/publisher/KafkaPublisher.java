package com.voidbank.transaction_api.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        log.info("Producing transaction event to topic {} - message {}", topic, message);

        kafkaTemplate.send(topic, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Kafka publish FAIL: {}", message, ex);
                    } else {
                        log.info("📤 Kafka publish OK: {}", message);
                    }
                });
    }

}
