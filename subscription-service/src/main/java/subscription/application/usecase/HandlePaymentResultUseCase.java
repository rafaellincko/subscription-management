package subscription.application.usecase;

//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import subscription.application.event.SubscriptionPaymentRequestEvent;
import subscription.application.event.SubscriptionPaymentResultEvent;
import subscription.application.exception.SubscriptionNotFoundException;
import subscription.application.port.in.SubscriptionPaymentEventConsumer;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.port.out.SubscriptionPaymentEventPublisher;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

public class HandlePaymentResultUseCase implements SubscriptionPaymentEventConsumer {

    private static final int MAX_ATTEMPTS = 3;

    private final SubscriptionRepository repository;
    private final ApplicationLogger logger;

    public HandlePaymentResultUseCase(
            SubscriptionRepository repository,
            SubscriptionPaymentEventPublisher eventPublisher,
            ApplicationLogger logger
    ) {
        this.repository = repository;
        this.logger = logger;
    }

    @Transactional
    @Override
    public void handle(SubscriptionPaymentResultEvent event) {

        Subscription subscription = repository.findById(event.subscriptionId())
                .orElseThrow(SubscriptionNotFoundException::new);

        if (subscription.getStatus() != SubscriptionStatus.RENOVANDO){
            logger.warn("Evento ignorado. Status atual: " + subscription.getStatus());
            return;
        }
        else {
            if (event.success()) {
                logger.info("Pagamento aprovado, renovando assinatura");

                subscription.renew();
                subscription.setStatus(SubscriptionStatus.valueOf("ATIVA"));
                repository.save(subscription);
                return;
            }

            subscription.registerFailure();

            if(subscription.getfailedRenewalAttempts() >= MAX_ATTEMPTS){
                logger.warn("Suspendendo assinatura após falhas consecutivas");
                repository.save(subscription);
                return;
            }
            subscription.setStatus(SubscriptionStatus.valueOf("ATIVA"));
            repository.save(subscription);
        }
    }
}

