package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.persistence.dao.VehicleTypeDAO;
import org.grp5.getacar.persistence.entity.VehicleType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

@Path("/rest/v1/vehicleTypes")
public class VehicleTypeResource {

    private final Provider<VehicleTypeDAO> vehicleTypeDAOProvider;

    @Inject
    public VehicleTypeResource(Provider<VehicleTypeDAO> vehicleTypeDAOProvider) {
        this.vehicleTypeDAOProvider = vehicleTypeDAOProvider;
    }

    public void addVehicleType(VehicleType vehicleType) {

    }

    public void changeVehicleType(VehicleType vehicleType) {

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public VehicleType getVehicleType(@PathParam("id") Integer id) {
        final VehicleTypeDAO vehicleTypeDAO = vehicleTypeDAOProvider.get();
        return vehicleTypeDAO.find(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Response getVehicleTypes() {
        final VehicleTypeDAO vehicleTypeDAO = vehicleTypeDAOProvider.get();
        return Response.status(Response.Status.OK).entity(Collections.singletonMap("vehicleTypes",
                vehicleTypeDAO.findAll())).build();
    }

    public void removeVehicleType() {

    }
}