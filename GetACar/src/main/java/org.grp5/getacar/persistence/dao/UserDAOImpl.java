package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.validation.Validator;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.User} entities.
 */
public class UserDAOImpl extends BaseDAOImpl<Integer, User> implements UserDAO {

    @Inject
    public UserDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        super(validator, entityManagerProvider, hibernateSessionProvider);
    }

    @Override
    public User findByLoginName(String loginName) {
        if (loginName == null) {
            return null;
        }
        final Session hibernateSession = getHibernateSession();
        final Criteria criteria = hibernateSession.createCriteria(getEntityClass());
        criteria.add(Restrictions.eq("loginName", loginName));

        return (User) criteria.uniqueResult();
    }
}