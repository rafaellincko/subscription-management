package subscription.interfaceadapter.dto.response;

import java.time.Instant;

public record ErrorResponse(
        String code,
        String message,
        Instant timestamp
) {}
