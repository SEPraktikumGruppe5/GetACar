package org.grp5.getacar.domain.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.Vehicle;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.validation.Validator;

/**
 * Data Access Object for {@link org.grp5.getacar.domain.Vehicle} domain objects.
 */
public class VehicleDAOImpl extends BaseDAOImpl<Integer, Vehicle> implements VehicleDAO {

    @Inject
    public VehicleDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                          Provider<Session> sessionProvider) {
        super(validator, entityManagerProvider, sessionProvider);
    }
}