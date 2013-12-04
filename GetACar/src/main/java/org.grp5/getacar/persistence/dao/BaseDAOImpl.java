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
public class BaseDAOImpl<K extends Serializable, D> implements BaseDAO<K, D> {

    private final Class<D> entityClass;
    private final Provider<EntityManager> entityManagerProvider;
    private final Provider<Session> hibernateSessionProvider;
    private final Validator validator;

    @SuppressWarnings("unchecked")
    public BaseDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                       Provider<Session> hibernateSessionProvider) {
        this.validator = validator;
        this.entityManagerProvider = entityManagerProvider;
        this.hibernateSessionProvider = hibernateSessionProvider;
        this.entityClass = (Class<D>) ClassHelper.getTypeArguments(BaseDAOImpl.class, getClass()).get(1);
    }

    public Class<D> getEntityClass() {
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
    public void create(D entity) {
        validate(entity);
        final Session hibernateSession = getHibernateSession();
        hibernateSession.save(entity);
        hibernateSession.flush();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void change(D entity) {
        validate(entity);
        final Session hibernateSession = getHibernateSession();
        hibernateSession.update(entity);
        hibernateSession.flush();
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public void remove(D entity) {
//        validate(entity); TODO: ?
        final Session hibernateSession = getHibernateSession();
        hibernateSession.delete(entity);
        hibernateSession.flush();
    }

    @Override
    public D find(K id) {
        EntityManager entityManager = getEntityManager();
        return entityManager.find(entityClass, id);
    }

    @Override
    @Transactional
    public D findCurrentDbStateInstance(K id) {
        final StatelessSession hibernateStatelessSession = getHibernateStatelessSession();
        final D entity = (D) hibernateStatelessSession.get(getEntityClass(), id, LockMode.READ);
        hibernateStatelessSession.close();
        return entity;
    }

    @Override
    public List<D> findAll() {
        EntityManager entityManager = getEntityManager();
        CriteriaQuery<D> query = entityManager.getCriteriaBuilder().createQuery(getEntityClass());
        query.select(query.from(getEntityClass()));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<D> findRange(int start, int end) {
        CriteriaQuery<D> query = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        query.select(query.from(getEntityClass()));
        TypedQuery<D> q = getEntityManager().createQuery(query);
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
        TypedQuery<D> q = getEntityManager().createQuery(query);
        try {
            return ((Long) q.getSingleResult()).intValue();
        } catch (NoResultException ex) {
            return 0;
        }
    }

    @Override
    public void remove(List<D> entities) {
        for (D entity : entities) {
            remove(entity);
        }
    }

    private void validate(D entity) throws ConstraintViolationException {
        final Set<ConstraintViolation<D>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(Sets.<ConstraintViolation<?>>newHashSet(constraintViolations));
        }
    }
}
