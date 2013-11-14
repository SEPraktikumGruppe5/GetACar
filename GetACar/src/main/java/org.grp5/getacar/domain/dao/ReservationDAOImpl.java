package org.grp5.getacar.domain.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.Reservation;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.validation.Validator;

/**
 * Data Access Object for {@link org.grp5.getacar.domain.Reservation} domain objects.
 */
public class ReservationDAOImpl extends BaseDAOImpl<Integer, Reservation> implements ReservationDAO {

    @Inject
    public ReservationDAOImpl(Validator validator, Provider<EntityManager> entityManagerProvider,
                              Provider<Session> hibernateSessionProvider) {
        super(validator, entityManagerProvider, hibernateSessionProvider);
    }
}