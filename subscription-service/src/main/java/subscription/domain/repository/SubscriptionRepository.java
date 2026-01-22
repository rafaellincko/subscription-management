package subscription.domain.repository;

import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    boolean existsActiveByUserId(UUID userId);

    Optional<Subscription> findActiveByUserId(UUID userId);

    Optional<Subscription> findByUserId(UUID userId);

    Optional<Subscription> findByUserIdToRenew(UUID userId);

    Optional<Subscription> findById(long Id);

    Subscription save(Subscription subscription);

    List<Subscription> findExpiredAndActive(LocalDate now);
}
