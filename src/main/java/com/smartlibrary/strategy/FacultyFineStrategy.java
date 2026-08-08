package com.smartlibrary.strategy;

import java.math.BigDecimal;

public class FacultyFineStrategy implements IFineStrategy {
    private static final BigDecimal RATE = BigDecimal.valueOf(20); // LKR/day
    public BigDecimal calculateFine(long daysLate) {
        if (daysLate <= 0) return BigDecimal.ZERO;
        return RATE.multiply(BigDecimal.valueOf(daysLate));
    }
}