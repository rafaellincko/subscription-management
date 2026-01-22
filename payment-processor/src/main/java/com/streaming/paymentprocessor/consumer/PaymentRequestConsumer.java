package com.streaming.paymentprocessor.consumer;

//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectMapper;
import com.streaming.paymentprocessor.event.SubscriptionPaymentRequestedEvent;
import com.streaming.paymentprocessor.event.SubscriptionPaymentResultEvent;
import com.streaming.paymentprocessor.producer.PaymentResultPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.springframework.boot.availability.AvailabilityChangeEvent.publish;

@Component
@Slf4j
public class PaymentRequestConsumer {

    private final PaymentResultPublisher resultPublisher;
    private final ObjectMapper objectMapper;

    public PaymentRequestConsumer(PaymentResultPublisher publisher,
                                  ObjectMapper objectMapper) {
        this.resultPublisher = publisher;

        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "subscription-payment-requested",
            groupId = "payment-processor-group"
    )
    public void consume(String payload) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        SubscriptionPaymentRequestedEvent event =
                objectMapper.readValue(payload, SubscriptionPaymentRequestedEvent.class);

        log.info("Requisição de pagamento recebida: {}", event);

        boolean approved = simulatePayment();

        resultPublisher.publish(new SubscriptionPaymentResultEvent(
                event.subscriptionId(),
                approved,
                event.attempt()+1
        ));

        log.info("Resultado de pagamento publicado | Aprovado={}", approved);
    }

    private boolean simulatePayment() {
        return Math.random() > 0.5; // 50% sucesso
    }
}