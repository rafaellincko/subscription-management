package com.streaming.paymentprocessor.event;

import java.math.BigDecimal;
import java.util.UUID;

public record SubscriptionPaymentRequestedEvent(
        long subscriptionId,
        String plan,
        BigDecimal amount,
        int attempt
) {}
