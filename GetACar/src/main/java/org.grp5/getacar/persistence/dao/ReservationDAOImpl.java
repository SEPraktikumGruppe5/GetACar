package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.hibernate.Session;

import javax.persistence.EntityManager;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.entity.Reservation} entities.
 */
public class ReservationDAOImpl extends BaseDAOImpl<Integer, Reservation> implements ReservationDAO {

    @Inject
    public ReservationDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                              Provider<Session> hibernateSessionProvider) {
        super(validationHelper, entityManagerProvider, hibernateSessionProvider);
    }
}