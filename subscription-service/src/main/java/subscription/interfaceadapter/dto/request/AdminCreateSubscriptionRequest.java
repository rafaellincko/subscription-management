package subscription.interfaceadapter.dto.request;

import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.util.UUID;

public record AdminCreateSubscriptionRequest(
        UUID userId,
        Plan plan,
        LocalDate startDate,
        LocalDate expirationDate,
        SubscriptionStatus status,
        int failedRenewalAttempts
) {}