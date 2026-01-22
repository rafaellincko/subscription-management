package subscription.application.usecase;

import subscription.domain.enums.Plan;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.UUID;

public class CreateSubscriptionUseCase {

    private final SubscriptionRepository repository;

    public CreateSubscriptionUseCase(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription execute(UUID userId, Plan plan) {
        if (repository.existsActiveByUserId(userId)) {
            throw new IllegalStateException("Usuário já possui assinatura ativa");
        }
        return repository.save(new Subscription(userId, plan));
    }
}