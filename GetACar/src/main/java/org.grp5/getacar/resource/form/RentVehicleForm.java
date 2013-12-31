package org.grp5.getacar.resource.form;

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
public class RentVehicleForm {

    private Position startPosition;
    private Position endPosition;
    private DateTime from;
    private DateTime to;

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