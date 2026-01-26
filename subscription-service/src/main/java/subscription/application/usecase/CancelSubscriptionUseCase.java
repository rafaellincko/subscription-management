package subscription.application.usecase;

import subscription.application.exception.SubscriptionNotFoundException;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.UUID;

public class CancelSubscriptionUseCase {

    private final SubscriptionRepository repository;

    public CancelSubscriptionUseCase(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription execute(UUID userId) {

        Subscription subscription =  repository.findActiveByUserId(userId)
                .orElseThrow(SubscriptionNotFoundException::new);

        subscription.cancel();

        return repository.save(subscription);
    }
}