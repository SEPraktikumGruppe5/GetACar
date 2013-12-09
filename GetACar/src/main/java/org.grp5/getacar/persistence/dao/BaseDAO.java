package org.grp5.getacar.persistence.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Base Data Access Object interface.
 */
public interface BaseDAO<K extends Serializable, E> {
    /**
     * Creates the entity.
     *
     * @param entity The entity to create
     */
    void create(E entity);

    /**
     * Changes the entity.
     *
     * @param entity The entity to change
     */
    void change(E entity);

    /**
     * Removes the entity.
     *
     * @param entity The entity to remove
     */
    void remove(E entity);

    /**
     * Removes the entities.
     *
     * @param entities The entities to remove
     */
    void remove(List<E> entities);

    /**
     * Find a entity by its primary key.
     *
     * @param id The primary key of the entity to find
     * @return The found entity or null if none was found for the given primary key
     */
    E find(K id);

    /**
     * Finds the entity with the given id.
     * <br />
     * CARE: As a {@link org.hibernate.StatelessSession} is used to get the entity, the collections can not be initialized
     * and have to be fetched separately.
     *
     * @param id The key of the entity to find
     * @return The found entity in detached state or null if none was found.
     */
    E findCurrentDbStateInstance(K id);

    /**
     * Finds all entities.
     *
     * @return A list containing all entities or an empty list is none was found
     */
    List<E> findAll();

    /**
     * Finds all entities in the given range.
     *
     * @param start The number of the found entities where the result list should begin
     * @param end   The number of the found entities where the result list should end
     * @return A list containing all entities in the given range or an empty list if none was found
     */
    public List<E> findRange(int start, int end);

    /**
     * Counts all entities
     *
     * @return The total number of entities available
     */
    public int count();
}