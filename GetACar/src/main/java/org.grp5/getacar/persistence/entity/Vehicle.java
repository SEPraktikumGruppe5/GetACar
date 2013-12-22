package org.grp5.getacar.persistence.entity;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Vehicle entity class.
 */
@Entity
@Table(name = "fahrzeug")
@AttributeOverride(name = "id", column = @Column(name = "f_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
public class Vehicle extends BaseEntity {

    private VehicleType vehicleType;
    private String licenseNumber;
    private List<VehicleImage> vehicleImages = Lists.newArrayList();
    private BigDecimal initialLatitude;
    private BigDecimal initialLongitude;
    private Boolean active;
    private String comment;

    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "ft_id", columnDefinition = "int(10) unsigned")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @NotNull
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Basic(optional = false)
    @Column(name = "f_kennzeichen", columnDefinition = "varchar(20)", nullable = false, updatable = true)
    @Pattern(regexp = "[A-Z]{1,3}[-][A-Z]{1,2} [1-9][0-9]{0,3}")
    @NotNull
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @OneToMany(mappedBy = "vehicle")
    @Valid
    @Size(min = 1, max = 5)
    public List<VehicleImage> getVehicleImages() {
        return vehicleImages;
    }

    public void setVehicleImages(List<VehicleImage> vehicleImages) {
        this.vehicleImages = vehicleImages;
    }

    @Basic(optional = false)
    @Column(name = "f_breitengrad_init", columnDefinition = "decimal(10,7)")
    @Digits(integer = 3, fraction = 7)
    @NotNull
    public BigDecimal getInitialLatitude() {
        return initialLatitude;
    }

    public void setInitialLatitude(BigDecimal initialLatitude) {
        this.initialLatitude = initialLatitude;
    }

    @Basic(optional = false)
    @Column(name = "f_laengengrad_init", columnDefinition = "decimal(10,7)")
    @Digits(integer = 3, fraction = 7)
    @NotNull
    public BigDecimal getInitialLongitude() {
        return initialLongitude;
    }

    public void setInitialLongitude(BigDecimal initialLongitude) {
        this.initialLongitude = initialLongitude;
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