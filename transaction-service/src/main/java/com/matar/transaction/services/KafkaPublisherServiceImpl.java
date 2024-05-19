package com.matar.transaction.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisherServiceImpl {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(Object object, String topic) {
        log.info("Publishing object to topic: '{}' with request: '{}'", topic, object);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, object);
        future.handleAsync((result, ex) -> {
            if (result != null) {
                //success
                log.info("Sent message=[" + object +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else if (ex != null) {
                //failure
                log.info("Unable to send message=[" +
                        object + "] due to : " + ex.getMessage());
            }
            return null;
            //whatever you want to return to complete your future (or throw to complete exceptionally)
        });
    }
}
