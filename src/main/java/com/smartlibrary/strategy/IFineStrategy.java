package com.smartlibrary.strategy;

import java.math.BigDecimal;

// Strategy interface for fine calculation.

public interface IFineStrategy {
    BigDecimal calculateFine(long daysLate);
}