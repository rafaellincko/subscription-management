package subscription.application.scheduler;


//import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import subscription.application.port.out.ApplicationLogger;
import subscription.application.usecase.RenewSubscriptionUseCase;
import subscription.domain.model.Subscription;
import subscription.domain.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SubscriptionRenewalScheduler {

    private final SubscriptionRepository repository;
    private final RenewSubscriptionUseCase renewUseCase;
    private final ApplicationLogger logger;

    public SubscriptionRenewalScheduler(SubscriptionRepository repository, RenewSubscriptionUseCase renewUseCase, ApplicationLogger logger) {
        this.repository = repository;
        this.renewUseCase = renewUseCase;
        this.logger = logger;
    }

    //@Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = "0 * * * * *")
    public void processRenewals(){
        List<Subscription> expired = repository.findExpiredAndActive(LocalDate.now());

        logger.info("Scheduler rodando em " + LocalDateTime.now());

        expired.forEach(renewUseCase::execute);
    }
}
