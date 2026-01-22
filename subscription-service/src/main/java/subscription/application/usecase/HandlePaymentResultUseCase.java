package subscription.application.usecase;

//import jakarta.transaction.Transactional;
import subscription.application.event.SubscriptionPaymentRequestEvent;
import subscription.application.event.SubscriptionPaymentResultEvent;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.port.out.SubscriptionPaymentEventPublisher;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

public class HandlePaymentResultUseCase  {

    private static final int MAX_ATTEMPTS = 3;

    private final SubscriptionRepository repository;
    private final SubscriptionPaymentEventPublisher eventPublisher;
    private final ApplicationLogger logger;

    public HandlePaymentResultUseCase(
            SubscriptionRepository repository,
            SubscriptionPaymentEventPublisher eventPublisher,
            ApplicationLogger logger
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.logger = logger;
    }

//    @Transactional
    public void handle(SubscriptionPaymentResultEvent event) {

        Subscription subscription = repository.findById(event.subscriptionId())
                .orElseThrow(()->
                        new RuntimeException("Assinatura não encontrada"));

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

