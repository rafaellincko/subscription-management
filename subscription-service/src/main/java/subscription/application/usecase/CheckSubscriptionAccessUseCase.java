package subscription.application.usecase;

import subscription.application.exception.SubscriptionNotFoundException;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.UUID;

public class CheckSubscriptionAccessUseCase {

    private final SubscriptionRepository repository;

    public CheckSubscriptionAccessUseCase(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public boolean execute(UUID userId, LocalDate today) {

        Subscription subscription = repository.findByUserId(userId)
                .orElseThrow(SubscriptionNotFoundException::new);

        return subscription.isActiveForUse(today);
    }
}