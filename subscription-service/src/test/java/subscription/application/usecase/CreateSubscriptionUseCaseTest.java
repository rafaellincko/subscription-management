package subscription.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.application.exception.ApplicationException;
import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSubscriptionUseCaseTest {

    @Mock
    private SubscriptionRepository repository;

    @InjectMocks
    private CreateSubscriptionUseCase useCase;

    @Test
    void shouldCreateSubscriptionIfNotExists() {
        UUID userId = UUID.randomUUID();
        when(repository.existsActiveByUserId(userId)).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        Subscription result = useCase.execute(userId, Plan.BASICO);

        assertEquals(userId, result.getUserId());
        assertEquals(Plan.BASICO, result.getPlan());
        assertEquals(SubscriptionStatus.ATIVA, result.getStatus());
    }

    @Test
    void shouldThrowIfActiveSubscriptionExists() {
        UUID userId = UUID.randomUUID();
        when(repository.existsActiveByUserId(userId)).thenReturn(true);

        assertThrows(ApplicationException.class, () -> useCase.execute(userId, Plan.BASICO));
    }
}
