package subscription.application.port.out;

public interface ApplicationLogger {

    void info (String message);

    void warn (String message);

    void error (String message);

    void error (String message, Throwable throwable);

}
