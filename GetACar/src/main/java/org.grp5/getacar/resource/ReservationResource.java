package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.Reservation;
import org.grp5.getacar.domain.dao.ReservationDAO;
import org.grp5.getacar.log.LogInvocation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/reservations")
public class ReservationResource {

    private final Provider<ReservationDAO> reservationDAOProvider;

    @Inject
    public ReservationResource(Provider<ReservationDAO> reservationDAOProvider) {
        this.reservationDAOProvider = reservationDAOProvider;
    }

    @GET
    @Path("/reservation/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Reservation getReservation(@PathParam("id") Integer id) {
        return reservationDAOProvider.get().find(id);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public List<Reservation> getReservations() {
        return reservationDAOProvider.get().findAll();
    }

    public void reserveVehicle(Reservation reservation) { // Könnte sein, dass wir die Parameter einzeln übergeben müssen, nicht als "fertiges" Reservation-Objekt!

    }
}