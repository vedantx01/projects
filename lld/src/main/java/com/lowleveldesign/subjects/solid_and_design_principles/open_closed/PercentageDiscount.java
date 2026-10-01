package com.lowleveldesign.subjects.solid_and_design_principles.open_closed;

import java.math.BigDecimal;
import java.util.Objects;

public final class PercentageDiscount implements DiscountPolicy {
    private final BigDecimal rate;

    public PercentageDiscount(BigDecimal rate) {
        this.rate = Objects.requireNonNull(rate, "rate");
        if (rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("rate must be between 0 and 1");
        }
    }

    @Override
    public BigDecimal apply(BigDecimal subtotal) {
        Objects.requireNonNull(subtotal, "subtotal");
        return subtotal.multiply(BigDecimal.ONE.subtract(rate));
    }
}
