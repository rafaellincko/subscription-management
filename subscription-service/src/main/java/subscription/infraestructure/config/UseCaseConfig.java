package subscription.infraestructure.config;

import org.springframework.kafka.core.KafkaTemplate;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.port.out.SubscriptionPaymentEventPublisher;
import subscription.application.usecase.*;
import subscription.domain.repository.SubscriptionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subscription.infraestructure.messaging.kafka.KafkaPaymentEventPublisher;
import tools.jackson.databind.ObjectMapper;


@Configuration
public class UseCaseConfig {

    @Bean
    CreateSubscriptionUseCase createSubscriptionUseCase(
            SubscriptionRepository repository
    ) {
        return new CreateSubscriptionUseCase(repository);
    }

    @Bean
    AdminCreateSubscriptionUseCase adminCreateSubscriptionUseCase(
            SubscriptionRepository repository
    ) {
        return new AdminCreateSubscriptionUseCase(repository);
    }

    @Bean
    AdminUpdateSubscriptionUseCase adminUpdateSubscriptionUseCase(
            SubscriptionRepository repository
    ) {
        return new AdminUpdateSubscriptionUseCase(repository);
    }

    @Bean
    GetSubscriptionUseCase getSubscriptionUseCase(
            SubscriptionRepository repository
    ) {
        return new GetSubscriptionUseCase(repository);
    }

    @Bean
    CancelSubscriptionUseCase cancelSubscriptionUseCase(
            SubscriptionRepository repository
    ) {
        return new CancelSubscriptionUseCase(repository);
    }

    @Bean
    CheckSubscriptionAccessUseCase checkSubscriptionAccessUseCase(
            SubscriptionRepository repository
    ) {
        return new CheckSubscriptionAccessUseCase(repository);
    }

    @Bean
    RenewSubscriptionUseCase renewSubscriptionUseCase(
            SubscriptionRepository repository,
            SubscriptionPaymentEventPublisher eventPublisher,
            ApplicationLogger logger
    ) {
        return new RenewSubscriptionUseCase(repository, eventPublisher, logger);
    }

    @Bean
    HandlePaymentResultUseCase handlePaymentResulUseCase(
            SubscriptionRepository repository,
            SubscriptionPaymentEventPublisher eventPublisher,
            ApplicationLogger logger
    ) {
        return new HandlePaymentResultUseCase(repository, eventPublisher, logger);
    }


    @Bean
    public SubscriptionPaymentEventPublisher kafkaPaymentEventPublisher(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        return new KafkaPaymentEventPublisher(kafkaTemplate, objectMapper);
    }

}
