package subscription.application.usecase;

import subscription.application.exception.ApplicationException;
import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;
import subscription.interfaceadapter.error.ErrorCode;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static subscription.interfaceadapter.error.ErrorCode.SUBSCRIPTION_INVALID_STATE;

public class AdminCreateSubscriptionUseCase {

    private final SubscriptionRepository repository;

    public AdminCreateSubscriptionUseCase(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription execute(UUID userId, Plan plan, LocalDate startDate, LocalDate expirationDate, SubscriptionStatus status, int failedRenewalAttempts) {

        if (repository.existsActiveByUserId(userId)) {
            throw new ApplicationException(SUBSCRIPTION_INVALID_STATE,"Usuário já possui assinatura ativa") {
            };
        }
        Optional<Subscription> subscription = repository.findByUserId(userId);
        return repository.save(new Subscription(
                userId,
                plan,
                startDate,
                expirationDate,
                status,
                failedRenewalAttempts));

    }
}

