package org.grp5.getacar.test.integration.resource;

import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.resource.ReservationResource;
import org.grp5.getacar.resource.form.Position;
import org.grp5.getacar.resource.form.ReserveVehicleForm;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@RunWith(OnamiRunner.class)
public class ReservationResourceIntegrationTest extends BaseResourceIntegrationTest implements Module {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
    }

    @Inject
    private static ReservationResource reservationResource;

    @BeforeClass
    public static void doBeforeClass() throws Exception {
        reservationResource = Mockito.spy(reservationResource);
        final User adminUser = new User();
        adminUser.setId(1);
        Mockito.doReturn(adminUser).when(reservationResource).getLoggedInUser();
    }

    @Test
    public void getReservationsTest() {
        final Response response = reservationResource.getReservations();
        Map<String, List<Reservation>> reservationsMap = (Map<String, List<Reservation>>) response.getEntity();
        final List<Reservation> reservations = reservationsMap.get("reservations");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong number of reservations", 8, reservations.size());
    }

    @Test
    public void getReservationSuccessTest() {
        final Response response = reservationResource.getReservation(1);
        Map<String, Reservation> reservationMap = (Map<String, Reservation>) response.getEntity();
        final Reservation reservation = reservationMap.get("reservation");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong or no reservation", 1, reservation != null ? reservation.getId() : -1);
    }

    @Test
    public void getReservationFailTest() {
        final Response response = reservationResource.getReservation(31337);
        Assert.assertEquals("Wrong status code", 404, response.getStatus());
        Assert.assertNull("Returned entity should be null", response.getEntity());
    }

    @Test
    public void getReservationByUserTest() {
        final Response response = reservationResource.getReservationsByUser();
        Map<String, List<Reservation>> reservationsMap = (Map<String, List<Reservation>>) response.getEntity();
        final List<Reservation> reservations = reservationsMap.get("reservations");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong number of reservations", 4, reservations.size());
    }

    @Test
    public void reserveVehicleSuccessTest() {
        final ReserveVehicleForm reserveVehicleForm = new ReserveVehicleForm();

        final Position position = new Position();
        position.setLatitude(new BigDecimal(50.8428159));
        position.setLongitude(new BigDecimal(12.89787739999997));
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(6);
        reserveVehicleForm.setVehicle(vehicle);
        reserveVehicleForm.setStartTime(new DateTime(2013, 12, 11, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        reserveVehicleForm.setEndTime(new DateTime(2013, 12, 13, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        reserveVehicleForm.setStartPosition(position);
        reserveVehicleForm.setEndPosition(position);

        final Response reserveResponse = reservationResource.reserveVehicle(reserveVehicleForm);

        Assert.assertEquals("Wrong reservation response status code", 201, reserveResponse.getStatus());
    }

    @Test
    public void reserveVehicleCollisionFailTest() {
        final ReserveVehicleForm firstReserveVehicleForm = new ReserveVehicleForm();

        final Position position = new Position();
        position.setLatitude(new BigDecimal(50.8428159));
        position.setLongitude(new BigDecimal(12.89787739999997));
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(6);
        firstReserveVehicleForm.setVehicle(vehicle);
        firstReserveVehicleForm.setStartTime(new DateTime(2013, 12, 11, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        firstReserveVehicleForm.setEndTime(new DateTime(2013, 12, 13, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        firstReserveVehicleForm.setStartPosition(position);
        firstReserveVehicleForm.setEndPosition(position);

        final Response firstReserveResponse = reservationResource.reserveVehicle(firstReserveVehicleForm);

        Assert.assertEquals("Wrong reservation response status code", 201, firstReserveResponse.getStatus());

        final ReserveVehicleForm secondReserveVehicleForm = new ReserveVehicleForm();

        secondReserveVehicleForm.setVehicle(vehicle);
        final DateTime startTime = new DateTime(2013, 12, 12, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone));
        secondReserveVehicleForm.setStartTime(startTime);
        final DateTime endTime = new DateTime(2013, 12, 12, 20, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone));
        secondReserveVehicleForm.setEndTime(endTime);
        secondReserveVehicleForm.setStartPosition(position);
        secondReserveVehicleForm.setEndPosition(position);

        Map<String, Class<? extends Annotation>> violatedPathsAndAnnotationClasses = Maps.newHashMap();
        try {
            reservationResource.reserveVehicle(secondReserveVehicleForm);
        } catch (ConstraintViolationException cve) {
            final Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violatedPathsAndAnnotationClasses.put(constraintViolation.getPropertyPath().toString(),
                        (constraintViolation).getConstraintDescriptor().getAnnotation().annotationType());
            }
        }

        Assert.assertEquals("Wrong number of violations", 2, violatedPathsAndAnnotationClasses.size());
        Assert.assertTrue("Missing expected violation start time", (violatedPathsAndAnnotationClasses.containsKey("startTime")));
        Assert.assertTrue("Missing expected violation end time", (violatedPathsAndAnnotationClasses.containsKey("endTime")));
    }

    @Test
    public void reserveVehicleTooFarInFutureFailTest() {
        final ReserveVehicleForm reserveVehicleForm = new ReserveVehicleForm();

        final Position position = new Position();
        position.setLatitude(new BigDecimal(50.8428159));
        position.setLongitude(new BigDecimal(12.89787739999997));
        final Vehicle vehicle = new Vehicle();
        vehicle.setId(6);
        reserveVehicleForm.setVehicle(vehicle);
        final DateTime startTime = new DateTime(2013, 12, 13, 0, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone));
        reserveVehicleForm.setStartTime(startTime);
        final DateTime endTime = new DateTime(2013, 12, 14, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone));
        reserveVehicleForm.setEndTime(endTime);
        reserveVehicleForm.setStartPosition(position);
        reserveVehicleForm.setEndPosition(position);

        Map<String, Object> violatedPathsAndAnnotationClasses = Maps.newHashMap();
        try {
            reservationResource.reserveVehicle(reserveVehicleForm);
        } catch (ConstraintViolationException cve) {
            final Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violatedPathsAndAnnotationClasses.put(constraintViolation.getPropertyPath().toString(),
                        (constraintViolation).getConstraintDescriptor().getAnnotation().annotationType());
            }
        }

        Assert.assertEquals("Wrong number of violations", 1, violatedPathsAndAnnotationClasses.size());
        Assert.assertTrue("Missing expected violation start time", (violatedPathsAndAnnotationClasses.containsKey("startTime")));
    }
}