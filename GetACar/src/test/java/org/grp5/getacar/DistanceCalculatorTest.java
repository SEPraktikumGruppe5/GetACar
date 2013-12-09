package org.grp5.getacar;

import org.grp5.getacar.service.DistanceCalculator;
import org.grp5.getacar.service.DistanceCalculatorImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class DistanceCalculatorTest {

    private DistanceCalculator distanceCalculator;

    @Before
    public void prepare() {
        distanceCalculator = new DistanceCalculatorImpl();
    }

    @After
    public void cleanUp() {
        distanceCalculator = null;
    }

    @Test
    public void distanceCalculateTest() {
        final double distance = distanceCalculator.calculateDistance(new BigDecimal(0.5), new BigDecimal(11.0));
        Assert.assertEquals(10.5d, distance, 0.123);
    }
}