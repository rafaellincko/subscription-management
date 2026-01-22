package subscription.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.application.event.SubscriptionPaymentResultEvent;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.port.out.SubscriptionPaymentEventPublisher;
import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class HandlePaymentResultUseCaseTest {

    @Mock
    private SubscriptionRepository repository;

    @Mock
    private SubscriptionPaymentEventPublisher publisher;

    @Mock
    private ApplicationLogger logger;

    @InjectMocks
    private HandlePaymentResultUseCase useCase;

    @Test
    void shouldRenewSubscriptionOnSuccess() {
        Subscription sub = new Subscription(1L, UUID.randomUUID(), Plan.BASICO, LocalDate.now(), LocalDate.now().minusDays(1), SubscriptionStatus.RENOVANDO, 0);
        when(repository.findById(1L)).thenReturn(Optional.of(sub));
        when(repository.save(sub)).thenReturn(sub);

        SubscriptionPaymentResultEvent event = new SubscriptionPaymentResultEvent(1L, true, 1);

        useCase.handle(event);

        assertEquals(SubscriptionStatus.ATIVA, sub.getStatus());
        assertEquals(0, sub.getfailedRenewalAttempts());
    }

    @Test
    void shouldIncrementFailureAndSuspendAfterMaxAttempts() {
        Subscription sub = new Subscription(1L, UUID.randomUUID(), Plan.BASICO, LocalDate.now(), LocalDate.now().minusDays(1), SubscriptionStatus.RENOVANDO, 2);
        when(repository.findById(1L)).thenReturn(Optional.of(sub));
        when(repository.save(sub)).thenReturn(sub);

        SubscriptionPaymentResultEvent event = new SubscriptionPaymentResultEvent(1L, false, 3);

        useCase.handle(event);

        assertEquals(SubscriptionStatus.SUSPENSA, sub.getStatus());
        assertEquals(3, sub.getfailedRenewalAttempts());
    }

    @Test
    void shouldIgnoreDuplicatedKafkaMessageWhenSubscriptionIsAlreadyActive() {
        // given
        Subscription subscription = new Subscription(
                1L,
                UUID.randomUUID(),
                Plan.BASICO,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                SubscriptionStatus.ATIVA, // 👈 já renovada
                0
        );

        when(repository.findById(1L)).thenReturn(Optional.of(subscription));

        SubscriptionPaymentResultEvent event =
                new SubscriptionPaymentResultEvent(1L, true, 1);

        // when
        useCase.handle(event);

        // then
        verify(repository, never()).save(any());
        verify(logger).warn(contains("Evento ignorado"));
    }
}