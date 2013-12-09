package org.grp5.getacar.persistence.dao;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.exception.NotImplementedException;
import org.grp5.getacar.persistence.Role;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.Role} entities.
 */
public class RoleDAOImpl extends BaseDAOImpl<Integer, Role> implements RoleDAO {

    @Inject
    public RoleDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        super(validationHelper, entityManagerProvider, hibernateSessionProvider);
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entity The role to be created
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void create(Role entity) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entity The role to be changed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void change(Role entity) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entity The role to be removed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void remove(Role entity) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available roles are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entities The roles to be removed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void remove(List<Role> entities) throws NotImplementedException {
        super.remove(entities);
    }

    @Override
    public Role findByName(String name) {
        final Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.add(Restrictions.eq("name", Preconditions.checkNotNull(name)));
        return (Role) criteria.uniqueResult();
    }
}