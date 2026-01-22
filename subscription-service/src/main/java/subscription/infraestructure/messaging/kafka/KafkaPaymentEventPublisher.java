package subscription.infraestructure.messaging.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import subscription.application.port.out.SubscriptionPaymentEventPublisher;
import subscription.application.event.SubscriptionPaymentRequestEvent;
import tools.jackson.databind.ObjectMapper;

public class KafkaPaymentEventPublisher
        implements SubscriptionPaymentEventPublisher {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaPaymentEventPublisher(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(SubscriptionPaymentRequestEvent event) {
        try {
            byte[] payload = objectMapper.writeValueAsBytes(event);

            kafkaTemplate.send(
                    "subscription-payment-requested",
                    String.valueOf(event.subscriptionId()),
                    payload
            );

        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize payment event", e);
        }
    }
}
