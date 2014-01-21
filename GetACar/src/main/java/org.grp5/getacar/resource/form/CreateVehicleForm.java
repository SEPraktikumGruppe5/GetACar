package org.grp5.getacar.resource.form;

import org.grp5.getacar.persistence.entity.Vehicle;

import javax.validation.Valid;

/**
 *
 */
public class CreateVehicleForm {
    private Vehicle vehicle = new Vehicle();

    @Valid
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
