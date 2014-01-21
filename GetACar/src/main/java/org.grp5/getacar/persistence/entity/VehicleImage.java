package org.grp5.getacar.persistence.entity;

import org.codehaus.jackson.annotate.JsonBackReference;

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

    private String fileName;
    private Vehicle vehicle;
    // TODO: Add description

    @ManyToOne(optional = false)
    @JoinColumn(name = "f_id", referencedColumnName = "f_id", columnDefinition = "int(10) unsigned",
            nullable = false, updatable = false)
    @NotNull
    @JsonBackReference // get and set have to be annotated
    public Vehicle getVehicle() {
        return vehicle;
    }

    @JsonBackReference // get and set have to be annotated
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

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
