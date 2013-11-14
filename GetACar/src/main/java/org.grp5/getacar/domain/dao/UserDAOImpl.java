package org.grp5.getacar.domain.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.validation.Validator;

/**
 * Data Access Object for {@link org.grp5.getacar.domain.User} domain objects.
 */
public class UserDAOImpl extends BaseDAOImpl<Integer, User> implements UserDAO {

    @Inject
    public UserDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        super(validator, entityManagerProvider, hibernateSessionProvider);
    }

    @Override
    public User findByLogin(String login) {
        if (login == null) {
            return null;
        }
        final Session hibernateSession = getHibernateSession();
        final Criteria criteria = hibernateSession.createCriteria(getDomainClass());
        criteria.add(Restrictions.eq("login", login));

        return (User) criteria.uniqueResult();
    }
}