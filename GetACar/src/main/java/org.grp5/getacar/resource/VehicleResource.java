package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.persistence.dao.VehicleDAO;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.grp5.getacar.resource.form.Position;
import org.grp5.getacar.resource.form.SearchVehiclesForm;
import org.grp5.getacar.service.TimeSimulator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/vehicles")
public class VehicleResource {

    private final Provider<VehicleDAO> vehicleDAOProvider;
    private final Provider<TimeSimulator> timeSimulatorProvider;
    private final ValidationHelper validationHelper;

    @Inject
    public VehicleResource(Provider<VehicleDAO> vehicleDAOProvider, Provider<TimeSimulator> timeSimulatorProvider,
                           ValidationHelper validationHelper) {
        this.vehicleDAOProvider = vehicleDAOProvider;
        this.timeSimulatorProvider = timeSimulatorProvider;
        this.validationHelper = validationHelper;
    }

    public void addVehicle(Vehicle vehicle) {

    }

    public void changeVehicle(Vehicle vehicle) {

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Vehicle getVehicle(@PathParam("id") Integer id) {
        return vehicleDAOProvider.get().find(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public List<Vehicle> getVehicles() {
        return vehicleDAOProvider.get().findAll();
    }

    public void removeVehicle(Vehicle vehicle) {

    }

    @POST
    @Path("/searchVehicles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public void searchVehicles(SearchVehiclesForm searchVehiclesForm) {
        validationHelper.validateAndThrow(searchVehiclesForm); // TODO: Do this by AOP!?
        final VehicleDAO vehicleDAO = vehicleDAOProvider.get();
        final Position position = searchVehiclesForm.getPosition();
        vehicleDAO.find(position.getLatitude(), position.getLongitude(), searchVehiclesForm.getRadius(),
                searchVehiclesForm.getVehicleType(), searchVehiclesForm.getFrom(), searchVehiclesForm.getTo(),
                timeSimulatorProvider.get().getTime());
    }
}
