package com.streaming.paymentprocessor.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.paymentprocessor.event.SubscriptionPaymentRequestedEvent;
import com.streaming.paymentprocessor.event.SubscriptionPaymentResultEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentResultPublisher {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentResultPublisher(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(SubscriptionPaymentResultEvent event) {
        try {
            byte[] payload = objectMapper.writeValueAsBytes(event);

            kafkaTemplate.send(
                    "subscription-payment-result",
                    String.valueOf(event.subscriptionId()),
                    payload
            );

        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize payment event", e);
        }
    }

}