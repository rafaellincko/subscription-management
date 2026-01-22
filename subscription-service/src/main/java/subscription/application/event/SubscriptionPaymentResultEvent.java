package subscription.application.event;

public record SubscriptionPaymentResultEvent(
        long subscriptionId,
        boolean success,
        int attempt
) {}
