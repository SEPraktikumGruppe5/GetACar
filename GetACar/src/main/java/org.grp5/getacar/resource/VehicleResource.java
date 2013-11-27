package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.Vehicle;
import org.grp5.getacar.domain.dao.VehicleDAO;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.service.TimeManager;

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
    private final Provider<TimeManager> timeManagerProvider;

    @Inject
    public VehicleResource(Provider<VehicleDAO> vehicleDAOProvider, Provider<TimeManager> timeManagerProvider) {
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

        final TimeManager timeManager = timeManagerProvider.get();
        final Date time = timeManager.getTime();
    }

    public void searchVehicle(/* Location location, */ Date startTime, Date endTime, Integer radius) {

    }
}