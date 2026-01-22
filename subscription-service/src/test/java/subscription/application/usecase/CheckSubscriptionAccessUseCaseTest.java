package subscription.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.domain.enums.Plan;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckSubscriptionAccessUseCaseTest {

    @Mock
    private SubscriptionRepository repository;

    @InjectMocks
    private CheckSubscriptionAccessUseCase useCase;

    @Test
    void shouldReturnTrueForActiveSubscription() {
        UUID userId = UUID.randomUUID();
        Subscription sub = new Subscription(userId, Plan.BASICO);
        when(repository.findByUserId(userId)).thenReturn(Optional.of(sub));

        boolean canAccess = useCase.execute(userId, LocalDate.now());

        assertTrue(canAccess);
    }

    @Test
    void shouldThrowWhenSubscriptionNotFound() {
        UUID userId = UUID.randomUUID();
        when(repository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> useCase.execute(userId, LocalDate.now()));
    }
}
