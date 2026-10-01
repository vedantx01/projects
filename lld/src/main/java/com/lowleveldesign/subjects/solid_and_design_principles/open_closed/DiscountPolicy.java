package com.lowleveldesign.subjects.solid_and_design_principles.open_closed;

import java.math.BigDecimal;

@FunctionalInterface
public interface DiscountPolicy {
    BigDecimal apply(BigDecimal subtotal);
}
