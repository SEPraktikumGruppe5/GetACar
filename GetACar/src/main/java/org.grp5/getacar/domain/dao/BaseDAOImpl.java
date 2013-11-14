package org.grp5.getacar.domain.dao;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.grp5.getacar.util.ClassHelper;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.List;

/**
 * Base Data Access Object implementation.
 */
public class BaseDAOImpl<K extends Serializable, D> implements BaseDAO<K, D> {

    private final Class<D> domainClass;
    private final Provider<EntityManager> entityManagerProvider;
    private final Provider<Session> hibernateSessionProvider;
    private final Validator validator;

    @SuppressWarnings("unchecked")
    public BaseDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        this.validator = validator;
        this.entityManagerProvider = entityManagerProvider;
        this.hibernateSessionProvider = hibernateSessionProvider;
        this.domainClass = (Class<D>) ClassHelper.getTypeArguments(BaseDAOImpl.class, getClass()).get(1);
    }

    public Class<D> getDomainClass() {
        return domainClass;
    }

    public Validator getValidator() {
        return validator;
    }

    protected EntityManager getEntityManager() {
        /*
        * Set session to FlushMode.MANUAL, otherwise selects can
        * raise errors as changes are committed before the transaction
        * ends on the fly etc
        */
        EntityManager entityManager = entityManagerProvider.get();
        entityManager.setFlushMode(FlushModeType.COMMIT);
        Session session = (Session) entityManager.getDelegate();
        session.setFlushMode(FlushMode.MANUAL);
        return entityManager;
    }

    protected Session getHibernateSession() {
        /*
        * See getEntityManager() description.
        */
        Session session = hibernateSessionProvider.get();
        session.setFlushMode(FlushMode.MANUAL);
        return session;
    }

    protected StatelessSession getHibernateStatelessSession() {
        return getHibernateSession().getSessionFactory().openStatelessSession();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void create(D domainObject) {
        final Session hibernateSession = getHibernateSession();
        hibernateSession.save(domainObject);
        hibernateSession.flush();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void change(D domainObject) {
        final Session hibernateSession = getHibernateSession();
        hibernateSession.update(domainObject);
        hibernateSession.flush();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void remove(D domainObject) {
        final Session hibernateSession = getHibernateSession();
        hibernateSession.delete(domainObject);
        hibernateSession.flush();
    }

    @Override
    public D find(K id) {
        EntityManager entityManager = getEntityManager();
        return entityManager.find(domainClass, id);
    }

    @Override
    @Transactional
    public D findCurrentDbStateInstance(K id) {
        final StatelessSession hibernateStatelessSession = getHibernateStatelessSession();
        final D domainObject = (D) hibernateStatelessSession.get(getDomainClass(), id, LockMode.READ);
        hibernateStatelessSession.close();
        return domainObject;
    }

    @Override
    public List<D> findAll() {
        EntityManager entityManager = getEntityManager();
        CriteriaQuery<D> query = entityManager.getCriteriaBuilder().createQuery(getDomainClass());
        query.select(query.from(getDomainClass()));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<D> findRange(int start, int end) {
        CriteriaQuery<D> query = getEntityManager().getCriteriaBuilder().createQuery(getDomainClass());
        query.select(query.from(getDomainClass()));
        TypedQuery<D> q = getEntityManager().createQuery(query);
        q.setMaxResults(end - start);
        q.setFirstResult(start);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int count() {
        CriteriaQuery query = getEntityManager().getCriteriaBuilder().createQuery(getDomainClass());
        Root rt = query.from(getDomainClass());
        query.select(getEntityManager().getCriteriaBuilder().count(rt));
        TypedQuery<D> q = getEntityManager().createQuery(query);
        try {
            return ((Long) q.getSingleResult()).intValue();
        } catch (NoResultException ex) {
            return 0;
        }
    }

    @Override
    public void remove(List<D> domainObjects) {
        for (D domainObject : domainObjects) {
            remove(domainObject);
        }
    }
}
