package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.grp5.getacar.domain.User;
import org.grp5.getacar.domain.dao.UserDAO;
import org.grp5.getacar.log.LogInvocation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/users")
public class UserResource {

    private final Provider<UserDAO> userDAOProvider;

    @Inject
    public UserResource(Provider<UserDAO> userDAOProvider) {
        this.userDAOProvider = userDAOProvider;
    }

    @POST
    @Path("/changeUserState")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresRoles("Admin")
    @Transactional(rollbackOn = {Exception.class})
    public void changeUserState(@QueryParam("id") Integer id, @QueryParam("state") boolean state) {
        final UserDAO userDAO = userDAOProvider.get();
        final User user = userDAO.find(id);
        if (user == null) {
            // error!!
            return;
        }
        user.setActive(state);
        userDAO.change(user);
    }

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresAuthentication
    public User getUser(@PathParam("id") Integer id) {
        return userDAOProvider.get().find(id);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresRoles("Admin")
    public List<User> getUsers() {
        return userDAOProvider.get().findAll();
    }

    public void loginUser(String login, String password) {

    }

    public void registerUser(User user) {

    }

    public void removeUser(Integer id) {
        // TODO: Benutzer <-> Rolle Verbindung entfernen
        // TODO: Reservierungen entfernen
    }
}