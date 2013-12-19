package org.grp5.getacar.persistence.dao;

import org.grp5.getacar.persistence.dto.VehicleSearchResult;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.entity.VehicleType;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.persistence.entity.Vehicle} entities.
 */
public interface VehicleDAO extends BaseDAO<Integer, Vehicle> {

    List<VehicleSearchResult> find(BigDecimal latitude, BigDecimal longitude, Integer radius, VehicleType vehicleType, DateTime from,
              DateTime to, DateTime atTime);
}
