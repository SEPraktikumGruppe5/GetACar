package org.grp5.getacar.persistence.dao;

import com.google.common.collect.Sets;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Base Data Access Object implementation.
 */
public class BaseDAOImpl<K extends Serializable, E> implements BaseDAO<K, E> {

    private final Class<E> entityClass;
    private final Provider<EntityManager> entityManagerProvider;
    private final Provider<Session> hibernateSessionProvider;
    private final Validator validator;

    @SuppressWarnings("unchecked")
    public BaseDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        this.validator = validator;
        this.entityManagerProvider = entityManagerProvider;
        this.hibernateSessionProvider = hibernateSessionProvider;
        this.entityClass = (Class<E>) ClassHelper.getTypeArguments(BaseDAOImpl.class, getClass()).get(1);
    }

    public Class<E> getEntityClass() {
        return entityClass;
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
    public void create(E entity) {
        validateAndThrow(entity);
        final Session hibernateSession = getHibernateSession();
        hibernateSession.save(entity);
        hibernateSession.flush();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void change(E entity) {
        validateAndThrow(entity);
        final Session hibernateSession = getHibernateSession();
        hibernateSession.update(entity);
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

    @Override
    public Set<ConstraintViolation<E>> validate(E entity) {
        return validator.validate(entity);
    }

    @Override
    public void validateAndThrow(E entity) throws ConstraintViolationException {
        final Set<ConstraintViolation<E>> constraintViolations = validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(Sets.<ConstraintViolation<?>>newHashSet(constraintViolations));
        }
    }
}