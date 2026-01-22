package subscription.interfaceadapter.controller;

import org.springframework.http.HttpStatus;
import subscription.application.usecase.CancelSubscriptionUseCase;
import subscription.application.usecase.CheckSubscriptionAccessUseCase;
import subscription.application.usecase.CreateSubscriptionUseCase;
import subscription.application.usecase.GetSubscriptionUseCase;
import subscription.domain.model.Subscription;
import subscription.interfaceadapter.dto.request.CreateSubscriptionRequest;
import subscription.interfaceadapter.dto.response.AccessResponse;
import subscription.interfaceadapter.dto.response.SubscriptionResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final CreateSubscriptionUseCase useCase;
    private final GetSubscriptionUseCase getUseCase;
    private final CancelSubscriptionUseCase cancelUseCase;
    private final CheckSubscriptionAccessUseCase checkSubscriptionAccessUseCase;

    public SubscriptionController(CreateSubscriptionUseCase useCase, GetSubscriptionUseCase getUseCase, CancelSubscriptionUseCase cancelUseCase, CheckSubscriptionAccessUseCase checkSubscriptionAccessUseCase) {
        this.useCase = useCase;
        this.getUseCase = getUseCase;
        this.cancelUseCase = cancelUseCase;
        this.checkSubscriptionAccessUseCase = checkSubscriptionAccessUseCase;
    }

    @PostMapping
    public SubscriptionResponse create(@RequestBody CreateSubscriptionRequest request) {
        return SubscriptionResponse.from(
                useCase.execute(request.userId(), request.plan())
        );
    }

    @GetMapping("/{userId}")
    public Subscription getByUser(@PathVariable UUID userId) {
        return getUseCase.execute(userId);
    }

    @GetMapping("/{userId}/access")
    public AccessResponse canAccess (@PathVariable UUID userId) {

        boolean canAccess = checkSubscriptionAccessUseCase.execute(
                userId,
                LocalDate.now()
        );
        return new AccessResponse(userId, canAccess);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Subscription cancel(@PathVariable UUID userId) {
        return cancelUseCase.execute(userId);
    }
}
