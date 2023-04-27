package com.ankhang.listener;


import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.ankhang.model.InfoModelTopicKafka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfoModelTopicKafkaListener {

    private final KafkaTemplate<String, InfoModelTopicKafka> kafkaTemplate;
    private final ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(InfoModelTopicKafka event) {
        log.info("InfoModelTopicKafka Event Received, Sending Event to notificationTopic with ID:" + event.getInfoId() + " Phone:" + event.getInfoPhone() + " Message:" + event.getMessageSend());
        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted("notification-topic-kafka", this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, InfoModelTopicKafka>> future = kafkaTemplate.send("notificationTopic",new InfoModelTopicKafka(event.getInfoId(),event.getInfoPhone(),event.getMessageSend()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}