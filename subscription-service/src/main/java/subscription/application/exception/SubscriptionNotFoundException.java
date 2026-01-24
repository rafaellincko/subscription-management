package subscription.application.exception;

import subscription.interfaceadapter.error.ErrorCode;

public class SubscriptionNotFoundException extends ApplicationException {

    public SubscriptionNotFoundException() {
        super(ErrorCode.SUBSCRIPTION_NOT_FOUND);
    }
}