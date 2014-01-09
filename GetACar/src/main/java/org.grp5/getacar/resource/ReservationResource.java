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
import java.util.List;

@Path("/rest/reservations")
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Reservation getReservation(@PathParam("id") Integer id) {
        return reservationDAOProvider.get().find(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public List<Reservation> getReservations() {
        return reservationDAOProvider.get().findAll();
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
        reservation.setStartTime(reserveVehicleForm.getFrom());
        reservation.setEndTime(reserveVehicleForm.getTo());
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
    private User getLoggedInUser() {
        return userDAOProvider.get().findByLoginName((String) SecurityUtils.getSubject().getPrincipal());
    }
}