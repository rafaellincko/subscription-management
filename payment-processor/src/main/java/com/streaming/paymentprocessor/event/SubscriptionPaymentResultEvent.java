package com.streaming.paymentprocessor.event;

import java.util.UUID;

public record SubscriptionPaymentResultEvent(
        long subscriptionId,
        boolean success,
        int attempt
) {}
