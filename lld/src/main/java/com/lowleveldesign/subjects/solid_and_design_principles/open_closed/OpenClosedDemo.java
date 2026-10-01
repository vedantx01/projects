package com.lowleveldesign.subjects.solid_and_design_principles.open_closed;

import java.math.BigDecimal;

public final class OpenClosedDemo {
    private OpenClosedDemo() {
    }

    public static void run() {
        DiscountPolicy loyaltyDiscount = new PercentageDiscount(new BigDecimal("0.10"));
        BigDecimal total = loyaltyDiscount.apply(new BigDecimal("100.00"));
        System.out.println("Open/Closed: discounted total = " + total);
    }
}
