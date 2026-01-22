package subscription.application.port.out;

import subscription.application.event.SubscriptionPaymentRequestEvent;

public interface SubscriptionPaymentEventPublisher {
    void publish(SubscriptionPaymentRequestEvent event);
}
