package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.entity.VehicleImage;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.hibernate.Session;

import javax.persistence.EntityManager;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.entity.VehicleImage} entities.
 */
public class VehicleImageDAOImpl extends BaseDAOImpl<Integer, VehicleImage> implements VehicleImageDAO {

    @Inject
    public VehicleImageDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                               Provider<Session> sessionProvider) {
        super(validationHelper, entityManagerProvider, sessionProvider);
    }
}