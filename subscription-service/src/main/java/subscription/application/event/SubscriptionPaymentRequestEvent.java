package subscription.application.event;

import java.math.BigDecimal;

public record SubscriptionPaymentRequestEvent(
        long subscriptionId,
        String plan,
        BigDecimal amount,
        int attempt
) {}
