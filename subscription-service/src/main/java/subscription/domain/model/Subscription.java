package subscription.domain.model;

import subscription.domain.enums.Plan;
import subscription.domain.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.util.UUID;

public class Subscription {

    private long id;

    private UUID userId;

    private Plan plan;

    private LocalDate startDate;
    private LocalDate expirationDate;

    private SubscriptionStatus status;

    private int failedRenewalAttempts;

    private Subscription() {}

    public Subscription(UUID userId, Plan plan) {
        this.userId = userId;
        this.plan = plan;
        this.startDate = LocalDate.now();
        this.expirationDate = startDate.plusMonths(1);
        this.status = SubscriptionStatus.ATIVA;
    }

    public Subscription(long id, UUID userId, Plan plan, LocalDate startDate, LocalDate expirationDate, SubscriptionStatus status, int failedRenewalAttempts) {
        this.id = id;
        this.userId = userId;
        this.plan = plan;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.failedRenewalAttempts = failedRenewalAttempts;
    }

    // getters
    public long getId() { return id; }
    public UUID getUserId() { return userId; }
    public Plan getPlan() { return plan; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public SubscriptionStatus getStatus() { return status; }
    public int getfailedRenewalAttempts() { return failedRenewalAttempts; }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public void cancel(){
        if (this.status != SubscriptionStatus.ATIVA){
            throw new IllegalStateException("Assinatura não pode ser cancelada");
        }
        this.status = SubscriptionStatus.CANCELADA;
    }

    public boolean isActiveForUse(LocalDate today){
        return !isExpired(today);
    }

    private boolean isExpired(LocalDate today) {
        return today.isAfter(this.expirationDate);
    }

    public void renew(){
        this.expirationDate=expirationDate.plusMonths(1);
        this.failedRenewalAttempts = 0;
    }

    public void registerFailure(){
        this.failedRenewalAttempts++;

        if(failedRenewalAttempts >= 3){
            this.status = SubscriptionStatus.SUSPENSA;
        }
    }

}