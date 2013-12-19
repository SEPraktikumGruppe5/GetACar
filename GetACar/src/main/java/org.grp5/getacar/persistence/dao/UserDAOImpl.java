package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.entity.User} entities.
 */
public class UserDAOImpl extends BaseDAOImpl<Integer, User> implements UserDAO {

    @Inject
    public UserDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        super(validationHelper, entityManagerProvider, hibernateSessionProvider);
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