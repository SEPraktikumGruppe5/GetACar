package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.persistence.dao.ReservationDAO;
import org.grp5.getacar.persistence.dao.UserDAO;
import org.grp5.getacar.persistence.entity.Reservation;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.grp5.getacar.resource.form.ReserveVehicleForm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/rest/v1/reservations")
public class ReservationResource {

    private final Provider<ReservationDAO> reservationDAOProvider;
    private final Provider<UserDAO> userDAOProvider;
    private final ValidationHelper validationHelper;

    @Inject
    public ReservationResource(Provider<ReservationDAO> reservationDAOProvider, Provider<UserDAO> userDAOProvider,
                               ValidationHelper validationHelper) {
        this.reservationDAOProvider = reservationDAOProvider;
        this.userDAOProvider = userDAOProvider;
        this.validationHelper = validationHelper;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Response getReservations() {
        final ReservationDAO reservationDAO = reservationDAOProvider.get();
        return Response.status(Response.Status.OK).entity(Collections.singletonMap("reservations",
                reservationDAO.findAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresAuthentication
    @LogInvocation
    public Response getReservation(@PathParam("id") Integer id) {
        final ReservationDAO reservationDAO = reservationDAOProvider.get();
        final Reservation reservation = reservationDAO.find(id);

        // reservation exists?
        if (reservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // is user allowed to show?
        final User loggedInUser = getLoggedInUser();
        if (!loggedInUser.equals(reservation.getUser())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.status(Response.Status.OK).entity(Collections.singletonMap("reservation",
                reservation)).build();
    }

    @GET
    @Path("/byUser")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresAuthentication
    @LogInvocation
    public Response getReservationsByUser() {
        // TODO: Use uid param / maybe do it like on the slides from mr. gaedke
        final ReservationDAO reservationDAO = reservationDAOProvider.get();
        final List<Reservation> reservations = reservationDAO.findByUser(getLoggedInUser());
        return Response.status(Response.Status.OK).entity(Collections.singletonMap("reservations",
                reservations)).build();
    }

    @POST
    @Path("/reserveVehicle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresAuthentication
    @LogInvocation
    public Response reserveVehicle(ReserveVehicleForm reserveVehicleForm) {
        validationHelper.validateAndThrow(reserveVehicleForm); // TODO: Do this by AOP!?

        final ReservationDAO reservationDAO = reservationDAOProvider.get();
        final User loggedInUser = getLoggedInUser();

        // create the reservation
        Reservation reservation = new Reservation();
        reservation.setUser(loggedInUser);
        reservation.setVehicle(reserveVehicleForm.getVehicle());
        reservation.setStartTime(reserveVehicleForm.getStartTime());
        reservation.setEndTime(reserveVehicleForm.getEndTime());
        reservation.setEndLatitude(reserveVehicleForm.getEndPosition().getLatitude());
        reservation.setEndLongitude(reserveVehicleForm.getEndPosition().getLongitude());

        reservationDAO.create(reservation);

        return Response.status(Response.Status.CREATED).build(); // TODO: Respond with this (201) always when successfully created!
    }

    /**
     * Gets the logged in {@link org.grp5.getacar.persistence.entity.User}.
     *
     * @return The logged in user or null if the user is not logged in.
     */
    public User getLoggedInUser() {
        final UserDAO userDAO = userDAOProvider.get();
        return userDAO.findByLoginName((String) SecurityUtils.getSubject().getPrincipal());
    }
}