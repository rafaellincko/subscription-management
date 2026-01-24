package subscription.application.port.in;

import subscription.application.event.SubscriptionPaymentResultEvent;

public interface SubscriptionPaymentEventConsumer {

    void handle(SubscriptionPaymentResultEvent event) throws Exception;
}
