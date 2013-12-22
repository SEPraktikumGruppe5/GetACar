package org.grp5.getacar.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * VehicleImage entity class.
 */
@Entity
@Table(name = "fahrzeugbild")
@AttributeOverride(name = "id", column = @Column(name = "fb_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
public class VehicleImage extends BaseEntity {

    private Vehicle vehicle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "f_id", referencedColumnName = "f_id", columnDefinition = "int(10) unsigned",
            nullable = false, updatable = false)
    @NotNull
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    private String fileName;

    @Basic(optional = false)
    @Column(name = "fb_dateiname", columnDefinition = "varchar(75)", unique = true)
    @Size(min = 1, max = 75)
    @NotNull
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }
}
