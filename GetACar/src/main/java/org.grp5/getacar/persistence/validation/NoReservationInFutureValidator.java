package org.grp5.getacar.persistence.validation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.beanutils.PropertyUtils;
import org.grp5.getacar.persistence.dao.ReservationDAO;
import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.joda.time.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 *
 */
public class NoReservationInFutureValidator extends BaseValidator implements
        ConstraintValidator<NoReservationInFuture, Reservation> {

    @Inject
    private Provider<ReservationDAO> reservationDAOProvider;
    @Inject
    private Provider<Session> hibernateSessionProvider;
    @Inject
    private Provider<StatelessSession> hibernateStatelessSessionProvider;

    private String startTimeField;
    private String message;

    @Override
    public void initialize(NoReservationInFuture constraintAnnotation) {
        startTimeField = constraintAnnotation.startTimeField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Reservation value, ConstraintValidatorContext context) {
        try {
            final DateTime startTime = (DateTime) PropertyUtils.getProperty(value, startTimeField);
            final ReservationDAO reservationDAO = reservationDAOProvider.get();
            final Vehicle vehicle = value.getVehicle();
            // can not validate without vehicle set
            if (vehicle == null) {
                return true;
            }
            final List<Reservation> reservationsAfter = reservationDAO.findReservationsAfter(vehicle, startTime);

            if (!reservationsAfter.isEmpty()) {
                addConstraintValidation(context, message, startTimeField);
                return false;
            }
        } catch (Exception ex) {
            // ignore
        }
        return true;
    }
}