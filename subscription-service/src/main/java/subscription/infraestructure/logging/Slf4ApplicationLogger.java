package subscription.infraestructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import subscription.application.port.out.ApplicationLogger;

@Component
public class Slf4ApplicationLogger implements ApplicationLogger {

    private static final Logger log =
            LoggerFactory.getLogger("APPLICATION_LOGGER");

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log.error(message, throwable);
    }
}
