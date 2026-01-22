package subscription.application.port.in;

public interface SubscriptionPaymentEventConsumer {

    void consume(String payload) throws Exception;
}
