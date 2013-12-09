package org.grp5.getacar.persistence.dao;

import org.grp5.getacar.persistence.Vehicle;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.persistence.Vehicle} entities.
 */
public interface VehicleDAO extends BaseDAO<Integer, Vehicle> {

    Object findByLicenseNumber(String licenseNumber);
}
