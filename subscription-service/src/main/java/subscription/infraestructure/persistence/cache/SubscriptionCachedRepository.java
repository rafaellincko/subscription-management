package subscription.infraestructure.persistence.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import subscription.application.port.out.ApplicationLogger;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class SubscriptionCachedRepository implements SubscriptionRepository{

    private final SubscriptionRepository delegate;
    private final CacheManager cacheManager;
    private final ApplicationLogger logger;

    public SubscriptionCachedRepository(SubscriptionRepository delegate, CacheManager cacheManager, ApplicationLogger logger) {
        this.delegate = delegate;
        this.cacheManager = cacheManager;
        this.logger = logger;
    }

    @Override
    public boolean existsActiveByUserId(UUID userId) {
        return delegate.existsActiveByUserId(userId);
    }

    @Cacheable(
            cacheNames = "subscription-active-by-user",
            key = "#userId"
    )
    public Optional<Subscription> findActiveByUserId(UUID userId) {
        return delegate.findActiveByUserId(userId);
    }

    @Cacheable(
            cacheNames = "subscription-by-userid",
            key = "#userId"
    )
    public Optional<Subscription> findByUserId(UUID userId) {
        return delegate.findByUserId(userId);
    }

    @Override
    public Optional<Subscription> findByUserIdToRenew(UUID userId) {
        return delegate.findByUserId(userId);
    }

//    @Cacheable(
//            cacheNames = "subscription-by-id",
//            key = "#id"
//    )
    @Override
    public Optional<Subscription> findById(long id) {
        return delegate.findById(id);
    }

//    @CacheEvict(
//            cacheNames = {
//                    "subscription-active-by-user",
//                    "subscription-by-id",
//                    "subscription-by-userid"
//            },
//            allEntries = true
//    )
@Caching(evict = {
        @CacheEvict(
                cacheNames = "subscription-by-id",
                key = "#subscription.id"
        ),
        @CacheEvict(
                cacheNames = {
                        "subscription-by-userid",
                        "subscription-active-by-user"
                },
                key = "#subscription.userId"
        )
})
    public Subscription save(Subscription subscription) {
        return delegate.save(subscription);
    }

    @Override
    public List<Subscription> findExpiredAndActive(LocalDate now) {
        return delegate.findExpiredAndActive(now);
    }
}
