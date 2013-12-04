package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.Vehicle;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.validation.Validator;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.Vehicle} entities.
 */
public class VehicleDAOImpl extends BaseDAOImpl<Integer, Vehicle> implements VehicleDAO {

    @Inject
    public VehicleDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                          Provider<Session> sessionProvider) {
        super(validator, entityManagerProvider, sessionProvider);
    }

    @Override
    public Vehicle findByLicenseNumber(String licenseNumber) {
        final Criteria criteria = getHibernateSession().createCriteria(Vehicle.class);
        criteria.add(Restrictions.eq("licenseNumber", licenseNumber));
        return (Vehicle) criteria.uniqueResult();
    }
}