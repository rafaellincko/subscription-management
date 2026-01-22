package subscription.domain.enums;

import java.math.BigDecimal;

public enum Plan {

    BASICO(new BigDecimal("19.90")),
    PREMIUM(new BigDecimal("39.90")),
    FAMILIA(new BigDecimal("59.90"));

    private final BigDecimal price;

    Plan(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}