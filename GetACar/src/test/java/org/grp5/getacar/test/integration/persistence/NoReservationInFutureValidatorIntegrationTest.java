package org.grp5.getacar.test.integration.persistence;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.validation.NoReservationInFuture;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@RunWith(OnamiRunner.class)
public class NoReservationInFutureValidatorIntegrationTest extends BaseValidatorIntegrationTest {

    @Inject
    ValidationHelper validationHelper;

    @Test
    public void invalidValueTest() {
        final Reservation reservation = new Reservation();
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        reservation.setVehicle(vehicle);
        timeSimulator.setTime(new DateTime(2013, 12, 12, 0, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        reservation.setStartTime(new DateTime(2013, 12, 13, 0, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));

        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(reservation);

        Map<String, Class<? extends Annotation>> violatedPathsAndAnnotationClasses = Maps.newHashMap();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            violatedPathsAndAnnotationClasses.put(constraintViolation.getPropertyPath().toString(),
                    (constraintViolation).getConstraintDescriptor().getAnnotation().annotationType());
        }
        Assert.assertTrue("Should be invalid", (violatedPathsAndAnnotationClasses.containsKey("startTime") &&
                violatedPathsAndAnnotationClasses.get("startTime").equals(NoReservationInFuture.class)));
    }

    @Test
    public void validValueTest() {
        final Reservation reservation = new Reservation();
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        reservation.setVehicle(vehicle);
        reservation.setStartTime(new DateTime(2013, 12, 28, 0, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));

        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(reservation);

        Map<String, Class<? extends Annotation>> violatedPathsAndAnnotationClasses = Maps.newHashMap();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            violatedPathsAndAnnotationClasses.put(constraintViolation.getPropertyPath().toString(),
                    (constraintViolation).getConstraintDescriptor().getAnnotation().annotationType());
        }
        Assert.assertTrue("Should be invalid", (!violatedPathsAndAnnotationClasses.containsValue(NoReservationInFuture.class)));
    }
}
