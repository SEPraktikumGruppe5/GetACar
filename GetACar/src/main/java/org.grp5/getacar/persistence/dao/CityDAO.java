package org.grp5.getacar.persistence.dao;

import org.grp5.getacar.persistence.City;

import java.util.List;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.persistence.City} entities.
 */
public interface CityDAO extends BaseDAO<Integer, City> {
    List<City> findByNamePart(String namePart, Integer maxResults);
}
