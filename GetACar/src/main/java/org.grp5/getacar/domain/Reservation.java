package org.grp5.getacar.domain;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Reservation domain object.
 */
@Entity
@Table(name = "reservierung")
@AttributeOverride(name = "id", column = @Column(name = "re_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Reservation extends BaseDomainObject {

    private User user;
    private Vehicle vehicle;
    private Date startTime;
    private Date endTime;
    private BigDecimal endPositionWidth;
    private BigDecimal endPositionLength;

    @ManyToOne(optional = false)
    @JoinColumn(name = "b_id", columnDefinition = "int(10) unsigned")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "f_id", columnDefinition = "int(10) unsigned")
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Basic(optional = false)
    @Column(name = "re_startzeit", columnDefinition = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Basic(optional = false)
    @Column(name = "re_endzeit", columnDefinition = "datetime")
    @NotNull
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Basic(optional = false)
    @Column(name = "re_endkoord_breite", columnDefinition = "decimal(9,6)", nullable = false, updatable = true)
    public BigDecimal getEndPositionWidth() {
        return endPositionWidth;
    }

    public void setEndPositionWidth(BigDecimal endPositionWidth) {
        this.endPositionWidth = endPositionWidth;
    }

    @Basic(optional = false)
    @Column(name = "re_endkoord_laenge", columnDefinition = "decimal(9,6)", nullable = false, updatable = true)
    public BigDecimal getEndPositionLength() {
        return endPositionLength;
    }

    public void setEndPositionLength(BigDecimal endPositionLength) {
        this.endPositionLength = endPositionLength;
    }
}