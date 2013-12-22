package org.grp5.getacar.persistence.transformer;

import org.grp5.getacar.persistence.dto.VehicleSearchResult;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.entity.VehicleType;
import org.hibernate.transform.BasicTransformerAdapter;

import java.math.BigDecimal;

/**
 *
 */
public class VehicleSearchResultTransformer extends BasicTransformerAdapter { // TODO: Write test for it!
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        final VehicleSearchResult vehicleSearchResult = new VehicleSearchResult();

        final Vehicle vehicle = new Vehicle();
        vehicle.setId((Integer) tuple[0]);
        vehicle.setActive((Boolean) tuple[1]);
        vehicle.setInitialLongitude((BigDecimal) tuple[2]);
        vehicle.setInitialLatitude((BigDecimal) tuple[3]);
        vehicle.setComment((String) tuple[4]);
        vehicle.setLicenseNumber((String) tuple[5]);
//        vehicle.setPicture((String) tuple[6]); // TODO: Join for images

        final VehicleType vehicleType = new VehicleType();
        vehicleType.setId((Integer) tuple[6]);
        vehicleType.setName((String) tuple[7]);
        vehicleType.setIcon((String) tuple[8]);
        vehicleType.setDescription((String) tuple[9]);

        vehicle.setVehicleType(vehicleType);

        vehicleSearchResult.setVehicle(vehicle);

        vehicleSearchResult.setDistance((Double) tuple[10]);
        vehicleSearchResult.setCurrentLatitude((BigDecimal) tuple[11]);
        vehicleSearchResult.setCurrentLongitude((BigDecimal) tuple[12]);

        return vehicleSearchResult;
    }
}
