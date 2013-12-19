package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.service.TimeSimulator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static javax.ws.rs.core.Response.Status.OK;

@Path("/rest/time")
public class TimeResource {

    private final Provider<TimeSimulator> timeSimulatorProvider;

    @Inject
    public TimeResource(Provider<TimeSimulator> timeSimulatorProvider) {
        this.timeSimulatorProvider = timeSimulatorProvider;

    }

    @GET
    @Path("/whatTimeIsIt")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresAuthentication
    public Response whatTimeIsIt() {
        final TimeSimulator timeSimulator = timeSimulatorProvider.get();
        return Response.status(OK).entity(Collections.singletonMap("time", timeSimulator.getTime()))
                .build();
    }
}