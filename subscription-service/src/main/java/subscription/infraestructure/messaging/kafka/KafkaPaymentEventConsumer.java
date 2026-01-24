package subscription.infraestructure.messaging.kafka;

import org.springframework.stereotype.Component;
import subscription.application.event.SubscriptionPaymentResultEvent;
import subscription.application.port.in.SubscriptionPaymentEventConsumer;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.usecase.HandlePaymentResultUseCase;
import tools.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;


@Component
public class KafkaPaymentEventConsumer {

    private final ObjectMapper objectMapper;
    private final ApplicationLogger logger;
    //private final HandlePaymentResultUseCase handlePaymentUseCase;
    private final SubscriptionPaymentEventConsumer subscriptionPaymentEventConsumer;

    public KafkaPaymentEventConsumer(ObjectMapper objectMapper, ApplicationLogger logger,
                                     //HandlePaymentResultUseCase handlePaymentUseCase,
                                     SubscriptionPaymentEventConsumer subscriptionPaymentEventConsumer) {
        this.objectMapper = objectMapper;
        this.logger = logger;
        //this.handlePaymentUseCase = handlePaymentUseCase;
        this.subscriptionPaymentEventConsumer = subscriptionPaymentEventConsumer;
    }

    //@Override
    @KafkaListener(
            topics = "subscription-payment-result",
            groupId = "subscription-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String payload) throws Exception {
        SubscriptionPaymentResultEvent event =
                objectMapper.readValue(payload, SubscriptionPaymentResultEvent.class);

        logger.info("Retorno de pagamento recebido: " + event.subscriptionId()
                + " | Success: " + event.success()
                + " | Tentativa: " + event.attempt());
        try {
            //handlePaymentUseCase.handle(event);
            subscriptionPaymentEventConsumer.handle(event);
        } catch (Exception e) {
            logger.error("Erro técnico, Kafka pode reprocessar", e);
            throw e;
        }

    }
    }

