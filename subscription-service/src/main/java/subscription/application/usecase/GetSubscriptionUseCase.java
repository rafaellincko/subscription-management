package subscription.application.usecase;

import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.UUID;

public class GetSubscriptionUseCase {

    private final SubscriptionRepository repository;

    public GetSubscriptionUseCase(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription execute(UUID userId) {

        return repository.findByUserId(userId)
                .orElseThrow(()->
                        new RuntimeException("Assinatura não encontrada"));
    }
}