package org.grp5.getacar.domain.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.Role;
import org.grp5.getacar.exception.NotImplementedException;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.validation.Validator;
import java.util.List;

/**
 * Data Access Object for {@link org.grp5.getacar.domain.Role} domain objects.
 */
public class RoleDAOImpl extends BaseDAOImpl<Integer, Role> implements RoleDAO {

    @Inject
    public RoleDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        super(validator, entityManagerProvider, hibernateSessionProvider);
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param domainObject The role to be created
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void create(Role domainObject) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param domainObject The role to be changed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void change(Role domainObject) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param domainObject The role to be removed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void remove(Role domainObject) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param domainObjects The roles to be removed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void remove(List<Role> domainObjects) throws NotImplementedException {
        super.remove(domainObjects);
    }
}