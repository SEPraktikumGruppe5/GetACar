package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.entity.VehicleType;
import org.grp5.getacar.persistence.validation.DateBeforeOtherDate;
import org.joda.time.DateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 */
@DateBeforeOtherDate.List(
        value = {@DateBeforeOtherDate(date = "from", otherDate = "to")}
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

    @NotNull
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @NotNull
    public DateTime getFrom() {
        return from;
    }

    public void setFrom(DateTime from) {
        this.from = from;
    }

    @NotNull
    public DateTime getTo() {
        return to;
    }

    public void setTo(DateTime to) {
        this.to = to;
    }
}