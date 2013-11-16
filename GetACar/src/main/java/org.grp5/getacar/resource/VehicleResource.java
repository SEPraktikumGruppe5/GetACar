package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.Vehicle;
import org.grp5.getacar.domain.dao.VehicleDAO;
import org.grp5.getacar.log.LogInvocation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/vehicles")
public class VehicleResource {

    private final Provider<VehicleDAO> carDAOProvider;

    @Inject
    public VehicleResource(Provider<VehicleDAO> carDAOProvider) {
        this.carDAOProvider = carDAOProvider;
    }

    @GET
    @Path("/vehicle/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Vehicle getCar(@PathParam("id") Integer id) {
        return carDAOProvider.get().find(id);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public List<Vehicle> getCars() {
        return carDAOProvider.get().findAll();
    }
}