package org.grp5.getacar.domain.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Base Data Access Object interface.
 */
public interface BaseDAO<K extends Serializable, D> {
    /**
     * Creates the domain object.
     *
     * @param domainObject The domain object to create
     */
    void create(D domainObject);

    /**
     * Changes the domain object.
     *
     * @param domainObject The domain object to change
     */
    void change(D domainObject);

    /**
     * Removes the domain object.
     *
     * @param domainObject The domain object to remove
     */
    void remove(D domainObject);

    /**
     * Removes the domain objects.
     *
     * @param domainObjects The domain objects to remove
     */
    void remove(List<D> domainObjects);

    /**
     * Find a domain object by its primary key.
     *
     * @param id The primary key of the domain object to find
     * @return The found domain object or null if none was found for the given primary key
     */
    D find(K id);

    /**
     * Finds the domain object with the given id.
     * <br />
     * CARE: As a {@link org.hibernate.StatelessSession} is used to get the domain object, the collections can not be initialized
     * and have to be fetched separately.
     *
     * @param id The key of the domain object to find
     * @return The found domain object in detached state or null if none was found.
     */
    D findCurrentDbStateInstance(K id);

    /**
     * Finds all domain objects.
     *
     * @return A list containing all domain objects or an empty list is none was found
     */
    List<D> findAll();

    /**
     * Finds all domain objects in the given range.
     *
     * @param start The number of the found domain objects where the result list should begin
     * @param end   The number of the found domain objects where the result list should end
     * @return A list containing all domain objects in the given range or an empty list if none was found
     */
    public List<D> findRange(int start, int end);

    /**
     * Counts all domain objects
     *
     * @return The total number of domain objects available
     */
    public int count();
}
