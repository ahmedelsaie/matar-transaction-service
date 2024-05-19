package com.matar.transaction.services.eventlisteners;

import com.matar.transaction.DomainEvent.TransactionChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionUpdatedListener {

    @RetryableTopic(
            attempts = "3",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            backoff =
            @Backoff(
                    delayExpression = "#{${spring.kafka.consumer.retryable-topic.backoff.delay}}",
                    multiplierExpression =
                            "#{${spring.kafka.consumer.retryable-topic.backoff.multiplier}}"),
            exclude = {
                    SerializationException.class,
                    DeserializationException.class
            })
    @KafkaListener(
            topics = "topic3",
            groupId = "1")
    public void consume(
            @Payload TransactionChangedEvent transactionChangedEvent, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

        log.info("Received transactionEventChanged event: {}, from topic: {}.", transactionChangedEvent, topic);
    }
}
