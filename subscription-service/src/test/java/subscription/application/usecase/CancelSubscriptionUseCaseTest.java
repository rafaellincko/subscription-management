package subscription.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelSubscriptionUseCaseTest {

    @Mock
    private SubscriptionRepository repository;

    @InjectMocks
    private CancelSubscriptionUseCase useCase;

    @Test
    void shouldCancelActiveSubscription() {
        UUID userId = UUID.randomUUID();
        Subscription sub = new Subscription(userId, Plan.BASICO);

        when(repository.findActiveByUserId(userId)).thenReturn(Optional.of(sub));
        when(repository.save(sub)).thenReturn(sub);

        Subscription result = useCase.execute(userId);

        assertEquals(SubscriptionStatus.CANCELADA, result.getStatus());
        verify(repository).save(sub);
    }

    @Test
    void shouldThrowWhenSubscriptionNotFound() {
        UUID userId = UUID.randomUUID();
        when(repository.findActiveByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.execute(userId));
    }
}