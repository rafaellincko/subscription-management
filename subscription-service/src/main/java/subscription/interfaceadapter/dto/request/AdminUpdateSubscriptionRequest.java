package subscription.interfaceadapter.dto.request;

import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;

import java.time.LocalDate;

public record AdminUpdateSubscriptionRequest(
        Plan plan,
        LocalDate startDate,
        LocalDate expirationDate,
        SubscriptionStatus status,
        int failedRenewalAttempts
) {}
