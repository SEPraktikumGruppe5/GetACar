package org.grp5.getacar.persistence.validation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.beanutils.PropertyUtils;
import org.grp5.getacar.persistence.dao.ReservationDAO;
import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.joda.time.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class NoReservationTimeCollisionValidator extends BaseValidator implements
        ConstraintValidator<NoReservationTimeCollision, Reservation> {

    @Inject
    private Provider<ReservationDAO> reservationDAOProvider;
    @Inject
    private Provider<Session> hibernateSessionProvider;
    @Inject
    private Provider<StatelessSession> hibernateStatelessSessionProvider;

    private String startTimeField;
    private String endTimeField;
    private String message;

    @Override
    public void initialize(NoReservationTimeCollision constraintAnnotation) {
        startTimeField = constraintAnnotation.startTimeField();
        endTimeField = constraintAnnotation.endTimeField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Reservation value, ConstraintValidatorContext context) {
        try {
            final DateTime startTime = (DateTime) PropertyUtils.getProperty(value, startTimeField);
            final DateTime endTime = (DateTime) PropertyUtils.getProperty(value, endTimeField);
            ClassMetadata meta = hibernateSessionProvider.get().getSessionFactory().getClassMetadata(value.getClass());
            final StatelessSession statelessSession = hibernateStatelessSessionProvider.get();
            Serializable id = meta.getIdentifier(value, (SessionImplementor) statelessSession);
            statelessSession.close();
            final ReservationDAO reservationDAO = reservationDAOProvider.get();
            final Vehicle vehicle = value.getVehicle();
            // can not validate without vehicle set
            if (vehicle == null) {
                return true;
            }
            final List<Reservation> collidingReservations =
                    reservationDAO.findCollidingReservations(vehicle, startTime, endTime);

            if (id == null) { // new reservation
                if (!collidingReservations.isEmpty()) {
                    addConstraintValidation(context, message, startTimeField);
                    addConstraintValidation(context, message, endTimeField);
                    return false;
                }
            } else { // reservation edit
                if (!collidingReservations.isEmpty()) {
                    if (collidingReservations.size() > 1) { // guaranteed that another one collides even if reservation itself is in the list
                        addConstraintValidation(context, message, startTimeField);
                        addConstraintValidation(context, message, endTimeField);
                        return false;
                    } else if (!collidingReservations.contains(value)) { // not empty, size not greater than 1 means size must be 1! If reservation itself is the one it is ok as we edit.
                        addConstraintValidation(context, message, startTimeField);
                        addConstraintValidation(context, message, endTimeField);
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            // ignore
        }
        return true;
    }
}