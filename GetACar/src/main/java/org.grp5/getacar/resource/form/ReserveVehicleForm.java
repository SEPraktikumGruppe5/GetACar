package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.validation.DateTimeBeforeOtherDateTime;
import org.grp5.getacar.persistence.validation.FutureDateTime;
import org.grp5.getacar.persistence.validation.MaxDaysFromNow;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 */
@DateTimeBeforeOtherDateTime.List(
        value = {@DateTimeBeforeOtherDateTime(date = "from", otherDate = "to")}
)
public class ReserveVehicleForm {

    private Vehicle vehicle;
    private Position startPosition;
    private Position endPosition;
    private DateTime from;
    private DateTime to;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Valid
    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    @Valid
    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    @NotNull
    @FutureDateTime
    @MaxDaysFromNow(2)
    public DateTime getFrom() {
        return from;
    }

    public void setFrom(DateTime from) {
        this.from = from;
    }

    @NotNull
    @FutureDateTime
    public DateTime getTo() {
        return to;
    }

    public void setTo(DateTime to) {
        this.to = to;
    }
}