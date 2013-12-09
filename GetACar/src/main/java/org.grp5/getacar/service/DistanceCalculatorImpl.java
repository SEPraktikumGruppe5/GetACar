package org.grp5.getacar.service;

import java.math.BigDecimal;

/**
 * Test implementation of the DistanceCalculator
 */
public class DistanceCalculatorImpl implements DistanceCalculator {

    @Override
    public Double calculateDistance(BigDecimal longitude, BigDecimal latitude) {
        return 10.5d;
    }
}