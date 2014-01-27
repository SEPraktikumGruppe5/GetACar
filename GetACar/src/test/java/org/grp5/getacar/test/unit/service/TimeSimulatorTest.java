package org.grp5.getacar.test.unit.service;

import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.service.TimeSimulator;
import org.grp5.getacar.service.TimeSimulatorImpl;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@RunWith(OnamiRunner.class)
public class TimeSimulatorTest {

    protected static TimeZone europeBerlinTimeZone = TimeZone.getTimeZone("Europe/Berlin");

    @Test
    public void defaultTimeTest() {
        final TimeSimulator timeSimulator = new TimeSimulatorImpl();
        final DateTime now = new DateTime();
        final DateTime simulatedTime = timeSimulator.getTime();
        Assert.assertTrue("Wrong default simulatedTime", simulatedTime.dayOfYear().equals(now.dayOfYear()) &&
                simulatedTime.getYear() == now.getYear());
    }

    @Test
    public void simulatedTimeTest() {
        final TimeSimulator timeSimulator = new TimeSimulatorImpl();
        timeSimulator.setTime(new DateTime(2013, 12, 12, 0, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        final DateTime setDate = new DateTime(2013, 12, 12, 0, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone));
        final DateTime simulatedTime = timeSimulator.getTime();
        Assert.assertTrue("Wrong simulatedTime", simulatedTime.toLocalDate().equals(setDate.toLocalDate()));
    }

    @Test
    public void timePassesTest() throws InterruptedException {
        final TimeSimulator timeSimulator = new TimeSimulatorImpl();
        final DateTime setTime = new DateTime(2013, 12, 12, 0, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone));
        timeSimulator.setTime(setTime);
        TimeUnit.SECONDS.sleep(1);
        final DateTime simulatedTime = timeSimulator.getTime();
        Assert.assertTrue("Wrong simulatedTime", simulatedTime.isAfter(setTime));
    }
}
