package org.grp5.getacar.persistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Vehicle entity class.
 */
@Entity
@Table(name = "fahrzeug")
@AttributeOverride(name = "id", column = @Column(name = "f_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
public class Vehicle extends BaseEntity {

    private String type;
    private String licenseNumber;
    private String picture;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean active;
    private String comment;

    @Basic(optional = false)
    @Column(name = "f_typ", columnDefinition = "varchar(100)")
    @NotNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic(optional = false)
    @Column(name = "f_kennzeichen", columnDefinition = "varchar(20)")
    @NotNull
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Basic(optional = false)
    @Column(name = "f_bild", columnDefinition = "text")
    @NotNull
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Basic(optional = true)
    @Column(name = "f_breitengrad", columnDefinition = "decimal(10,7)")
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Basic(optional = true)
    @Column(name = "f_laengengrad", columnDefinition = "decimal(10,7)")
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Basic(optional = false)
    @Column(name = "f_aktiv", columnDefinition = "bit(1) default 0")
    @NotNull
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Basic(optional = false)
    @Column(name = "f_bemerkung", columnDefinition = "text")
    @NotNull
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}