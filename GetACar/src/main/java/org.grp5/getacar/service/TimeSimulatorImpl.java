package org.grp5.getacar.service;

import com.google.inject.Inject;
import org.joda.time.DateTime;

public class TimeSimulatorImpl implements TimeSimulator {

    private DateTime simulatedTime = null;

    @Inject
    public TimeSimulatorImpl() {
    }

    public void setTime(DateTime time) {
        this.simulatedTime = time;
    }

    @Override
    public DateTime getTime() {
        final DateTime now = new DateTime();
        if (simulatedTime == null) {
            return now;
        }
        return simulatedTime.plus(now.getMillis() - simulatedTime.getMillis());
    }
}
