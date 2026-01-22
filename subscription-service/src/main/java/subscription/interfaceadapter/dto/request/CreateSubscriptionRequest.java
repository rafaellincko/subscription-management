package subscription.interfaceadapter.dto.request;


import subscription.domain.enums.Plan;

import java.util.UUID;

public record CreateSubscriptionRequest(
        UUID userId,
        Plan plan
) {}
