package org.grp5.getacar.persistence.entity;

import org.grp5.getacar.persistence.validation.MaxDaysFromNow;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Reservation entity class.
 */
@Entity
@Table(name = "reservierung")
@AttributeOverride(name = "id", column = @Column(name = "re_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
public class Reservation extends BaseEntity {

    private User user;
    private Vehicle vehicle;
    private DateTime startTime;
    private DateTime endTime;
    private BigDecimal endLongitude;
    private BigDecimal endLatitude;

    @ManyToOne(optional = false)
    @JoinColumn(name = "b_id", columnDefinition = "int(10) unsigned", nullable = false, updatable = false)
    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "f_id", columnDefinition = "int(10) unsigned", nullable = false, updatable = true)
    @NotNull
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Basic(optional = false)
    @Column(name = "re_startzeit", columnDefinition = "datetime", nullable = false, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @NotNull
    @MaxDaysFromNow(2)
    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    @Basic(optional = false)
    @Column(name = "re_endzeit", columnDefinition = "datetime", nullable = false, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @NotNull
    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    @Basic(optional = false)
    @Column(name = "re_end_breitengrad", columnDefinition = "decimal(10,7)", nullable = false, updatable = true)
    @NotNull
    public BigDecimal getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(BigDecimal endPositionWidth) {
        this.endLatitude = endPositionWidth;
    }

    @Basic(optional = false)
    @Column(name = "re_end_laengengrad", columnDefinition = "decimal(10,7)", nullable = false, updatable = true)
    @NotNull
    public BigDecimal getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(BigDecimal endPositionLength) {
        this.endLongitude = endPositionLength;
    }
}