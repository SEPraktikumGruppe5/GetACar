package org.grp5.getacar.persistence.dto;

import org.grp5.getacar.persistence.entity.Vehicle;

/**
 *
 */
public class VehicleSearchResult {
    private Vehicle vehicle;
    private Double distance;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
