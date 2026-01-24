package subscription.application.usecase;

import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.UUID;

public class CancelSubscriptionUseCase {

    private final SubscriptionRepository repository;

    public CancelSubscriptionUseCase(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription execute(UUID userId) {

        Subscription subscription =  repository.findByUserId(userId)
                .orElseThrow(()->
                        new RuntimeException("Assinatura não encontrada"));

        subscription.cancel();

        return repository.save(subscription);
    }
}