package org.grp5.getacar.persistence.dao;

import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Interface for Data Access Object for {@link org.grp5.getacar.persistence.entity.Reservation} entities.
 */
public interface ReservationDAO extends BaseDAO<Integer, Reservation> {
    List<Reservation> findCollidingReservations(Vehicle vehicle, DateTime startTime, DateTime endTime);

    List<Reservation> findReservationsAfter(Vehicle vehicle, DateTime startTime);

    List<Reservation> findByUser(User user);
}
