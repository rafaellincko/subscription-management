package subscription.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.application.exception.SubscriptionNotFoundException;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.port.out.SubscriptionPaymentEventPublisher;
import subscription.domain.enums.Plan;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RenewSubscriptionUseCaseTest {

    @Mock
    private SubscriptionRepository repository;

    @Mock
    private SubscriptionPaymentEventPublisher eventPublisher;

    @Mock
    private ApplicationLogger logger;

    @InjectMocks
    private RenewSubscriptionUseCase useCase;

    @Test
    void shouldPublishPaymentEvent() {
        Subscription sub = new Subscription(UUID.randomUUID(), Plan.BASICO);
        when(repository.findByUserIdToRenew(sub.getUserId())).thenReturn(Optional.of(sub));

        useCase.execute(sub);

        verify(eventPublisher).publish(any());
        verify(logger).info(anyString());
    }

    @Test
    void shouldThrowIfSubscriptionNotFound() {
        Subscription sub = new Subscription(UUID.randomUUID(), Plan.BASICO);
        when(repository.findByUserIdToRenew(sub.getUserId())).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> useCase.execute(sub));
    }
}
