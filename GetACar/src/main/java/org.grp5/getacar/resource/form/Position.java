package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.validation.SpecialConstraint;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 */
public class Position {
    private BigDecimal longitude;
    private BigDecimal latitude;

    // payload is read in org.grp5.getacar.resource.mapper.ValidationExceptionMapper which cuts off one level of the
    // propertyPath if he finds this! Not really scalable to more levels but does its job for now!
    @NotNull(payload = {SpecialConstraint.CombinedField.class})
    public BigDecimal getLongitude() {
        if (longitude != null) {
            return longitude.setScale(7, RoundingMode.UP);
        } else {
            return null;
        }
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the latitude value rounded up at scale 7.
     *
     * @return The latitude value rounded up at scale 7
     */
    @NotNull(payload = {SpecialConstraint.CombinedField.class})
    public BigDecimal getLatitude() {
        if (latitude != null) {
            return latitude.setScale(7, RoundingMode.UP);
        } else {
            return null;
        }
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        Position position = (Position) o;

        if (getLatitude() != null ? !getLatitude().equals(position.getLatitude()) : position.getLatitude() != null)
            return false;
        if (getLongitude() != null ? !getLongitude().equals(position.getLongitude()) : position.getLongitude() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getLongitude() != null ? getLongitude().hashCode() : 0;
        result = 31 * result + (getLatitude() != null ? getLatitude().hashCode() : 0);
        return result;
    }
}
