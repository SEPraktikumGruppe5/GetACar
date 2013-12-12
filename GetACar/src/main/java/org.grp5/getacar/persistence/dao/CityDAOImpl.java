package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.City;
import org.grp5.getacar.persistence.exception.NotImplementedException;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.City} entities.
 */
public class CityDAOImpl extends BaseDAOImpl<Integer, City> implements CityDAO {

    private final static int DEFAULT_MAX_RESULTS = 10;
    private final static int MAX_RESULTS = 50;

    @Inject
    public CityDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        super(validationHelper, entityManagerProvider, hibernateSessionProvider);
    }

    /**
     * Intentionally not implemented. Available cities are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entity The city to be created
     * @throws org.grp5.getacar.persistence.exception.NotImplementedException When the method is invoked
     */
    @Override
    public void create(City entity) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available cities are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entity The city to be changed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void change(City entity) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available cities are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entity The city to be removed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void remove(City entity) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Intentionally not implemented. Available cities are prepared before the app is deployed and may not be
     * changed afterwards.
     *
     * @param entities The cities to be removed
     * @throws NotImplementedException When the method is invoked
     */
    @Override
    public void remove(List<City> entities) throws NotImplementedException {
        super.remove(entities);
    }

    @Override
    public List<City> findByNamePart(String namePart, Integer maxResults) { // TODO: MinLength, not null?
        if (namePart == null) {
            return Collections.EMPTY_LIST;
        }
        final Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.add(Restrictions.or(
                Restrictions.ilike("name", namePart, MatchMode.ANYWHERE),
                Restrictions.ilike("asciiName", namePart, MatchMode.ANYWHERE)));
        criteria.addOrder(Order.desc("population"));
        if (maxResults == null) {
            maxResults = DEFAULT_MAX_RESULTS;
        } else if (maxResults > MAX_RESULTS) {
            maxResults = MAX_RESULTS;
        }
        criteria.setMaxResults(maxResults);
        return criteria.list();
    }
}