package org.grp5.getacar.persistence.dao;

import org.grp5.getacar.persistence.User;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.persistence.User} entities.
 */
public interface UserDAO extends BaseDAO<Integer, User> {

    /**
     * Finds a {@link User} by his login name.
     *
     * @param loginName The login name
     * @return The user with the given login name or null if none was found
     */
    User findByLoginName(String loginName);
}
