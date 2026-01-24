package subscription.application.usecase;

import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class AdminUpdateSubscriptionUseCase {

    private final SubscriptionRepository repository;

    public AdminUpdateSubscriptionUseCase(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription execute(UUID userId, Plan plan, LocalDate startDate, LocalDate expirationDate, SubscriptionStatus status, int failedRenewalAttempts) {

        Subscription subscription =  repository.findByUserId(userId)
                .orElseThrow(()->
                        new RuntimeException("Assinatura não encontrada"));

        return repository.save(new Subscription(
                userId,
                plan,
                startDate,
                expirationDate,
                status,
                failedRenewalAttempts));

    }
}
