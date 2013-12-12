package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.persistence.dao.CityDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

@Path("/rest/geo")
public class GeoResource {

    private final Provider<CityDAO> cityDAOProvider;

    @Inject
    public GeoResource(Provider<CityDAO> cityDAOProvider) {
        this.cityDAOProvider = cityDAOProvider;
    }

    @GET
    @Path("/cities")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Response getCities(@QueryParam("namePart") String namePart, @QueryParam("maxResults") Integer maxResults) {
        final CityDAO cityDAO = cityDAOProvider.get();
        return Response.status(Response.Status.OK).entity(Collections.singletonMap("cities",
                cityDAO.findByNamePart(namePart, maxResults))).build();
    }
}