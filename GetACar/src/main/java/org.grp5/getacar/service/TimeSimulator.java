package org.grp5.getacar.service;

import org.joda.time.DateTime;

/**
 *
 */
public interface TimeSimulator {
    DateTime getTime();

    void setTime(DateTime time);
}
