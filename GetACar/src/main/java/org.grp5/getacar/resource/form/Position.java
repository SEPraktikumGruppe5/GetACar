package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.validation.SpecialConstraint;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @NotNull(payload = {SpecialConstraint.CombinedField.class})
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
