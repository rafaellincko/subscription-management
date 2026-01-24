package subscription.application.exception;


import subscription.interfaceadapter.error.ErrorCode;

public abstract class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    protected ApplicationException(ErrorCode errorCode) {
        super(errorCode.defaultMessage());
        this.errorCode = errorCode;
    }

    protected ApplicationException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}