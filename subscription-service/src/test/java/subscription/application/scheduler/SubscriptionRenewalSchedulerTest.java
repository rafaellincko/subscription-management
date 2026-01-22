package subscription.application.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.usecase.RenewSubscriptionUseCase;
import subscription.domain.enums.Plan;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionRenewalSchedulerTest {

    @Mock
    private SubscriptionRepository repository;

    @Mock
    private RenewSubscriptionUseCase renewUseCase;

    @Mock
    private ApplicationLogger logger;

    @InjectMocks
    private SubscriptionRenewalScheduler scheduler;

    @Test
    void shouldProcessExpiredSubscriptions() {
        Subscription sub = new Subscription(UUID.randomUUID(), Plan.BASICO);
        List<Subscription> expiredSubs = List.of(sub);

        when(repository.findExpiredAndActive(any())).thenReturn(expiredSubs);

        scheduler.processRenewals();

        verify(renewUseCase).execute(sub);
        verify(logger).info(anyString());
    }
}