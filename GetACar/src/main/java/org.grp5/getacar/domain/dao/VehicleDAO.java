package org.grp5.getacar.domain.dao;

import org.grp5.getacar.domain.Vehicle;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.domain.Vehicle} domain objects.
 */
public interface VehicleDAO extends BaseDAO<Integer, Vehicle> {

    Object findByLicenseNumber(String licenseNumber);
}
