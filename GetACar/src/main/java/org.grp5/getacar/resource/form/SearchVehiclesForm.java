package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.entity.VehicleType;
import org.grp5.getacar.persistence.validation.BothOrNone;
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
        value = {@DateTimeBeforeOtherDateTime(date = "startTime", otherDate = "endTime")}
)
@BothOrNone.List(
        @BothOrNone(firstField = "startTime", secondField = "endTime")
)
public class SearchVehiclesForm {

    private Position position = new Position();
    private Integer radius;
    private VehicleType vehicleType;
    private DateTime startTime;
    private DateTime endTime;

    @Valid
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @NotNull
    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @FutureDateTime
    @MaxDaysFromNow(2)
    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    @FutureDateTime
    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }
}