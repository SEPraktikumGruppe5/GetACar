package org.grp5.getacar.persistence.dao;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.grp5.getacar.persistence.util.ClassHelper;
import org.grp5.getacar.persistence.validation.ValidationHelper;
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
import java.io.Serializable;
import java.util.List;

/**
 * Base Data Access Object implementation.
 */
public class BaseDAOImpl<K extends Serializable, E> implements BaseDAO<K, E> {

    private final Class<E> entityClass;
    private final Provider<EntityManager> entityManagerProvider;
    private final Provider<Session> hibernateSessionProvider;
    private final ValidationHelper validationHelper;

    @SuppressWarnings("unchecked")
    public BaseDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        this.validationHelper = validationHelper;
        this.entityManagerProvider = entityManagerProvider;
        this.hibernateSessionProvider = hibernateSessionProvider;
        this.entityClass = (Class<E>) ClassHelper.getTypeArguments(BaseDAOImpl.class, getClass()).get(1);
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public ValidationHelper getValidationHelper() {
        return validationHelper;
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
    public void create(E entity) {
        validationHelper.validateAndThrow(entity);
        final Session hibernateSession = getHibernateSession();
        hibernateSession.save(entity);
        hibernateSession.flush();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void change(E entity) {
        validationHelper.validateAndThrow(entity);
        final Session hibernateSession = getHibernateSession();
        hibernateSession.merge(entity);
        hibernateSession.flush();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void remove(E entity) {
//        validate(entity); TODO: ?
        final Session hibernateSession = getHibernateSession();
        hibernateSession.delete(entity);
        hibernateSession.flush();
    }

    @Override
    public E find(K id) {
        EntityManager entityManager = getEntityManager();
        return entityManager.find(entityClass, id);
    }

    @Override
    @Transactional
    public E findCurrentDbStateInstance(K id) {
        final StatelessSession hibernateStatelessSession = getHibernateStatelessSession();
        final E entity = (E) hibernateStatelessSession.get(getEntityClass(), id, LockMode.READ);
        hibernateStatelessSession.close();
        return entity;
    }

    @Override
    public List<E> findAll() {
        EntityManager entityManager = getEntityManager();
        CriteriaQuery<E> query = entityManager.getCriteriaBuilder().createQuery(getEntityClass());
        query.select(query.from(getEntityClass()));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<E> findRange(int start, int end) {
        CriteriaQuery<E> query = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        query.select(query.from(getEntityClass()));
        TypedQuery<E> q = getEntityManager().createQuery(query);
        q.setMaxResults(end - start);
        q.setFirstResult(start);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int count() {
        CriteriaQuery query = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        Root rt = query.from(getEntityClass());
        query.select(getEntityManager().getCriteriaBuilder().count(rt));
        TypedQuery<E> q = getEntityManager().createQuery(query);
        try {
            return ((Long) q.getSingleResult()).intValue();
        } catch (NoResultException ex) {
            return 0;
        }
    }

    @Override
    public void remove(List<E> entities) {
        for (E entity : entities) {
            remove(entity);
        }
    }
}