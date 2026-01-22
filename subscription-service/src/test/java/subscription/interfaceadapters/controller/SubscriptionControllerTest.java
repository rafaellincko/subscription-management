package subscription.interfaceadapters.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.application.usecase.*;
import subscription.domain.enums.Plan;
import subscription.domain.model.Subscription;
import subscription.interfaceadapter.controller.SubscriptionController;
import subscription.interfaceadapter.dto.request.CreateSubscriptionRequest;
import subscription.interfaceadapter.dto.response.AccessResponse;
import subscription.interfaceadapter.dto.response.SubscriptionResponse;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

    @Mock
    private CreateSubscriptionUseCase createUseCase;
    @Mock
    private GetSubscriptionUseCase getUseCase;
    @Mock
    private CancelSubscriptionUseCase cancelUseCase;
    @Mock
    private CheckSubscriptionAccessUseCase checkUseCase;

    @InjectMocks
    private SubscriptionController controller;

    @Test
    void shouldReturnCreatedSubscription() {
        UUID userId = UUID.randomUUID();
        Subscription sub = new Subscription(userId, Plan.BASICO);

        when(createUseCase.execute(userId, Plan.BASICO)).thenReturn(sub);

        CreateSubscriptionRequest request = new CreateSubscriptionRequest(userId, Plan.BASICO);
        SubscriptionResponse response = controller.create(request);

        // Comparando os campos do SubscriptionResponse com Subscription
        assertEquals(sub.getUserId(), response.userId());
        assertEquals(sub.getPlan().toString(), response.plan());
        assertEquals(sub.getStatus().toString(), response.status());
    }

    @Test
    void shouldCheckAccess() {
        UUID userId = UUID.randomUUID();
        when(checkUseCase.execute(userId, LocalDate.now())).thenReturn(true);

        AccessResponse response = controller.canAccess(userId);

        assertTrue(response.canAccess());
    }
}
