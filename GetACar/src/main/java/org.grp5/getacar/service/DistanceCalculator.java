package org.grp5.getacar.service;

import java.math.BigDecimal;

/**
 *
 */
public interface DistanceCalculator {
    Double calculateDistance(BigDecimal longitude, BigDecimal latitude);
}