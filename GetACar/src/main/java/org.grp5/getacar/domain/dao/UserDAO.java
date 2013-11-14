package org.grp5.getacar.domain.dao;

import org.grp5.getacar.domain.User;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.domain.User} domain objects.
 */
public interface UserDAO extends BaseDAO<Integer, User> {

    /**
     * Finds a {@link User} by his login name.
     *
     * @param login The login name
     * @return The user with the given login name or null if none was found or null was given as the login name
     *         parameter
     */
    User findByLogin(String login);
}
