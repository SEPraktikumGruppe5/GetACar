package org.grp5.getacar.domain;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Reservation domain object.
 */
@Entity
@Table(name = "Reservierung")
@AttributeOverride(name = "id", column = @Column(name = "RID",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Reservation extends BaseDomainObject {

    private User user;
    private Vehicle vehicle;
    private Date startTime;
    private Date endTime;
    private String startCoordinates;
    private String endCoordinates;

    @ManyToOne(optional = false)
    @JoinColumn(name = "UID", columnDefinition = "int(10) unsigned")
    @ForeignKey(name = "FK_UID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "CID", columnDefinition = "int(10) unsigned")
    @ForeignKey(name = "FK_CID")
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Basic(optional = false)
    @Column(name = "Startzeit", columnDefinition = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Basic(optional = false)
    @Column(name = "Endzeit", columnDefinition = "datetime")
    @NotNull
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Basic(optional = false)
    @Column(name = "Startkoordinaten", columnDefinition = "varchar(40)")
    @NotNull
    public String getStartCoordinates() {
        return startCoordinates;
    }

    public void setStartCoordinates(String startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    @Basic(optional = false)
    @Column(name = "Endkoordinaten", columnDefinition = "varchar(40)")
    @NotNull
    public String getEndCoordinates() {
        return endCoordinates;
    }

    public void setEndCoordinates(String endCoordinates) {
        this.endCoordinates = endCoordinates;
    }
}