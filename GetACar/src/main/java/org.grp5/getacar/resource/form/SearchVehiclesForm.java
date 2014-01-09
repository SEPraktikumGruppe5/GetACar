package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.entity.VehicleType;
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
public class SearchVehiclesForm {

    private Position position;
    private Integer radius;
    private VehicleType vehicleType;
    private DateTime from;
    private DateTime to;

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

    @NotNull // TODO: Can be null + multiple vehicle types
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
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