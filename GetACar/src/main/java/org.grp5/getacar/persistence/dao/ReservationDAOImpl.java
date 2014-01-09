package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.internal.util.$Preconditions;
import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.hibernate.Session;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.entity.Reservation} entities.
 */
public class ReservationDAOImpl extends BaseDAOImpl<Integer, Reservation> implements ReservationDAO {

    @Inject
    public ReservationDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                              Provider<Session> hibernateSessionProvider) {
        super(validationHelper, entityManagerProvider, hibernateSessionProvider);
    }

    @Override
    public List<Reservation> findCollidingReservations(Vehicle vehicle, DateTime startTime, DateTime endTime) {
        final TypedQuery<Reservation> collidingReservationsQuery =
                getEntityManager().createNamedQuery("Reservation.findCollidingReservations", getEntityClass());
        collidingReservationsQuery.setParameter("vehicle", $Preconditions.checkNotNull(vehicle))
                .setParameter("startTime", startTime).setParameter("endTime", endTime);
        return collidingReservationsQuery.getResultList();
    }
}