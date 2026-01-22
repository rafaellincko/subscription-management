package subscription.interfaceadapter.dto.response;

import java.util.UUID;

public record AccessResponse(
        UUID userId,
        boolean canAccess
    ) {

}
