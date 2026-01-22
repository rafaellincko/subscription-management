package subscription.domain.model;

import org.junit.jupiter.api.Test;
import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    @Test
    void shouldCancelActiveSubscription() {
        Subscription sub = new Subscription(UUID.randomUUID(), Plan.BASICO);

        sub.cancel();

        assertEquals(SubscriptionStatus.CANCELADA, sub.getStatus());
    }

    @Test
    void shouldNotCancelIfNotActive() {
        Subscription sub = new Subscription(UUID.randomUUID(), Plan.BASICO);
        sub.cancel();

        assertThrows(IllegalStateException.class, sub::cancel);
    }

    @Test
    void shouldRenewSubscription() {
        Subscription sub = new Subscription(
                1L,
                UUID.randomUUID(),
                Plan.BASICO,
                LocalDate.now().minusMonths(1),
                LocalDate.now(),
                SubscriptionStatus.RENOVANDO,
                2
        );

        sub.renew();

        assertEquals(0, sub.getfailedRenewalAttempts());
        assertTrue(sub.getExpirationDate().isAfter(LocalDate.now()));
    }

    @Test
    void shouldSuspendAfterThreeFailures() {
        Subscription sub = new Subscription(UUID.randomUUID(), Plan.BASICO);

        sub.registerFailure();
        sub.registerFailure();
        sub.registerFailure();

        assertEquals(SubscriptionStatus.SUSPENSA, sub.getStatus());
    }
}
