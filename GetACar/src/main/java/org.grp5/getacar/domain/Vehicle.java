package org.grp5.getacar.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Vehicle domain object.
 */
@Entity
@Table(name = "fahrzeug")
@AttributeOverride(name = "id", column = @Column(name = "FID",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Vehicle extends BaseDomainObject {

    private Integer number;
    private String type;
    private String licenseNumber;
    private String picture;
    private String actualCoordinates;
    private Boolean active;
    private String comment;

    @Basic(optional = false)
    @Column(name = "Nummer", columnDefinition = "int(10)")
    @NotNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Basic(optional = false)
    @Column(name = "Typ", columnDefinition = "varchar(100)")
    @NotNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic(optional = false)
    @Column(name = "Kennzeichen", columnDefinition = "varchar(20)")
    @NotNull
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Basic(optional = false)
    @Column(name = "Bild", columnDefinition = "text")
    @NotNull
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Basic(optional = false)
    @Column(name = "Aktuelle_Koordinaten", columnDefinition = "varchar(40)")
    @NotNull
    public String getActualCoordinates() {
        return actualCoordinates;
    }

    public void setActualCoordinates(String actualCoordinates) {
        this.actualCoordinates = actualCoordinates;
    }

    @Basic(optional = false)
    @Column(name = "Aktiv", columnDefinition = "tinyint(1) default 0")
    @NotNull
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Basic(optional = false)
    @Column(name = "Bemerkung", columnDefinition = "text")
    @NotNull
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}