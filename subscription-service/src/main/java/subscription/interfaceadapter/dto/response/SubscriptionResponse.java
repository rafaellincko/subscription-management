package subscription.interfaceadapter.dto.response;

import subscription.domain.model.Subscription;

import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(
        UUID userId,
        String plan,
        LocalDate startDate,
        LocalDate expirationDate,
        String status
) {
    public static SubscriptionResponse from(Subscription s) {
        return new SubscriptionResponse(
                s.getUserId(),
                s.getPlan().name(),
                s.getStartDate(),
                s.getExpirationDate(),
                s.getStatus().name()
        );
    }
}
