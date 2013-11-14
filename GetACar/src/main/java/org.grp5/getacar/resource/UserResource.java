package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.domain.User;
import org.grp5.getacar.domain.dao.UserDAO;
import org.grp5.getacar.log.LogInvocation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/users")
public class UserResource {

    private final Provider<UserDAO> userDAOProvider;

    @Inject
    public UserResource(Provider<UserDAO> userDAOProvider) {
        this.userDAOProvider = userDAOProvider;
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public User getUser(@PathParam("id") Integer id) {
        return userDAOProvider.get().find(id);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public List<User> getUsers() {
        return userDAOProvider.get().findAll();
    }
}