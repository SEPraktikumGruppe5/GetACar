package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.Vehicle;
import org.grp5.getacar.persistence.dao.VehicleDAO;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.service.TimeSimulator;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("/rest/vehicles")
public class VehicleResource {

    private final Provider<VehicleDAO> vehicleDAOProvider;
    private final Provider<TimeSimulator> timeManagerProvider;

    @Inject
    public VehicleResource(Provider<VehicleDAO> vehicleDAOProvider, Provider<TimeSimulator> timeManagerProvider) {
        this.vehicleDAOProvider = vehicleDAOProvider;
        this.timeManagerProvider = timeManagerProvider;
    }

    public void addVehicle(Vehicle vehicle) {

    }

    public void changeVehicle(Vehicle vehicle) {

    }

    @GET
    @Path("/vehicle/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Vehicle getVehicle(@PathParam("id") Integer id) {
        return vehicleDAOProvider.get().find(id);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public List<Vehicle> getVehicles() {
        return vehicleDAOProvider.get().findAll();
    }

    public void removeVehicle() {
//        final GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("DE"));
//        final Date time = gregorianCalendar.getTime();

        final TimeSimulator timeSimulator = timeManagerProvider.get();
        final DateTime time = timeSimulator.getTime();
    }

    public void searchVehicle(/* Location location, */ Date startTime, Date endTime, Integer radius) {

    }
}