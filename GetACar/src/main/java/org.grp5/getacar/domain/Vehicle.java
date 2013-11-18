package org.grp5.getacar.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Vehicle domain object.
 */
@Entity
@Table(name = "fahrzeug")
@AttributeOverride(name = "id", column = @Column(name = "f_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Vehicle extends BaseDomainObject {

    private Integer number;
    private String type;
    private String licenseNumber;
    private String picture;
    private BigDecimal actualPositionWidth;
    private BigDecimal actualPositionLength;
    private Boolean active;
    private String comment;

    @Basic(optional = false)
    @Column(name = "f_nummer", columnDefinition = "int(10)")
    @NotNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

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
    @Column(name = "f_akt_koord_breite", columnDefinition = "decimal(9,6)")
    public BigDecimal getActualPositionWidth() {
        return actualPositionWidth;
    }

    public void setActualPositionWidth(BigDecimal actualPositionWidth) {
        this.actualPositionWidth = actualPositionWidth;
    }

    @Basic(optional = true)
    @Column(name = "f_akt_koord_laenge", columnDefinition = "decimal(9,6)")
    public BigDecimal getActualPositionLength() {
        return actualPositionLength;
    }

    public void setActualPositionLength(BigDecimal actualPositionLength) {
        this.actualPositionLength = actualPositionLength;
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