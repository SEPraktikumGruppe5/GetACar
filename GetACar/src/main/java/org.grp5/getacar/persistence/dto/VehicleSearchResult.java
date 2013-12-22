package org.grp5.getacar.persistence.dto;

import org.grp5.getacar.persistence.entity.Vehicle;

import java.math.BigDecimal;

/**
 *  Represents a vehicle search result.
 */
public class VehicleSearchResult {
    private Vehicle vehicle;
    private BigDecimal currentLongitude;
    private BigDecimal currentLatitude;
    private Double distance;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public BigDecimal getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimal getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
