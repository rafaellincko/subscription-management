package subscription.infraestructure.messaging.kafka;

import org.springframework.stereotype.Component;
import subscription.application.event.SubscriptionPaymentResultEvent;
import subscription.application.port.in.SubscriptionPaymentEventConsumer;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.usecase.HandlePaymentResultUseCase;
import tools.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;


@Component
public class KafkaPaymentEventConsumer implements SubscriptionPaymentEventConsumer {

    private final ObjectMapper objectMapper;
    private final ApplicationLogger logger;
    private final HandlePaymentResultUseCase handlePaymentUseCase;

    public KafkaPaymentEventConsumer(ObjectMapper objectMapper, ApplicationLogger logger, HandlePaymentResultUseCase handlePaymentUseCase) {
        this.objectMapper = objectMapper;
        this.logger = logger;
        this.handlePaymentUseCase = handlePaymentUseCase;
    }

    @Override
    @KafkaListener(
            topics = "subscription-payment-result",
            groupId = "subscription-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String payload) {
        SubscriptionPaymentResultEvent event =
                objectMapper.readValue(payload, SubscriptionPaymentResultEvent.class);

        logger.info("Retorno de pagamento recebido: " + event.subscriptionId()
                + " | Success: " + event.success()
                + " | Tentativa: " + event.attempt());
        try {
            handlePaymentUseCase.handle(event);
        } catch (Exception e) {
            logger.error("Erro técnico, Kafka pode reprocessar", e);
            throw e;
        }

    }
    }

