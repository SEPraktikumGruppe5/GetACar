package org.grp5.getacar.persistence.dao;

import org.grp5.getacar.persistence.Role;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.persistence.Role} entities.
 */
public interface RoleDAO extends BaseDAO<Integer, Role> {
    Role findByName(String roleName);
}
