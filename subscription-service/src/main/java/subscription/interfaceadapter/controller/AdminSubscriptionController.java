package subscription.interfaceadapter.controller;

import org.springframework.web.bind.annotation.*;

import subscription.application.usecase.AdminCreateSubscriptionUseCase;
import subscription.application.usecase.AdminUpdateSubscriptionUseCase;
import subscription.interfaceadapter.dto.request.AdminCreateSubscriptionRequest;
import subscription.interfaceadapter.dto.request.AdminUpdateSubscriptionRequest;
import subscription.interfaceadapter.dto.response.SubscriptionResponse;

import java.util.UUID;

@RestController
@RequestMapping("/admin/subscriptions")
public class AdminSubscriptionController {

    private final AdminCreateSubscriptionUseCase createUseCase;
    private final AdminUpdateSubscriptionUseCase updateUseCase;

    public AdminSubscriptionController(AdminCreateSubscriptionUseCase createUseCase, AdminUpdateSubscriptionUseCase updateUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
    }

    @PostMapping
    public SubscriptionResponse create(@RequestBody AdminCreateSubscriptionRequest request) {
        return SubscriptionResponse.from(
                createUseCase.execute(
                        request.userId(),
                        request.plan(),
                        request.startDate(),
                        request.expirationDate(),
                        request.status(),
                        request.failedRenewalAttempts()
                )
        );
    }

    @PutMapping("/{userId}")
    public SubscriptionResponse update(@RequestBody AdminUpdateSubscriptionRequest request, @PathVariable UUID userId) {
        return SubscriptionResponse.from(
                updateUseCase.execute(
                        userId,
                        request.plan(),
                        request.startDate(),
                        request.expirationDate(),
                        request.status(),
                        request.failedRenewalAttempts()
                )
        );
    }
}
