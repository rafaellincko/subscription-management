package subscription.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import subscription.application.exception.SubscriptionNotFoundException;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.port.out.SubscriptionPaymentEventPublisher;
import subscription.application.event.SubscriptionPaymentRequestEvent;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

public class RenewSubscriptionUseCase {

    private final SubscriptionRepository repository;
    private final SubscriptionPaymentEventPublisher eventPublisher;
    private final ApplicationLogger logger;


    public RenewSubscriptionUseCase(SubscriptionRepository repository, SubscriptionPaymentEventPublisher eventPublisher, ApplicationLogger logger) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.logger = logger;
    }

    @Transactional
    public void execute(Subscription subscription){

    subscription = repository.findByUserIdToRenew(subscription.getUserId())
            .orElseThrow(SubscriptionNotFoundException::new);

        logger.info("Publicando evento de renovação de assinatura " + subscription.getId());

        eventPublisher.publish(
                new SubscriptionPaymentRequestEvent(
                        subscription.getId(),
                        subscription.getPlan().name(),
                        subscription.getPlan().getPrice(),
                        subscription.getfailedRenewalAttempts()
                )
        );
    }
}
