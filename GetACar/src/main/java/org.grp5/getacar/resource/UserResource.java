package org.grp5.getacar.resource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.persistence.User;
import org.grp5.getacar.persistence.dao.RoleDAO;
import org.grp5.getacar.persistence.dao.UserDAO;
import org.grp5.getacar.util.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Path("/rest/users")
public class UserResource {

    private final Provider<UserDAO> userDAOProvider;
    private final Provider<RoleDAO> roleDAOProvider;
    private final Provider<HttpServletRequest> servletRequestProvider;
    private final Provider<HttpServletResponse> servletResponseProvider;
    private final Validator validator;
    private final String successURL;

    @Inject
    public UserResource(Provider<UserDAO> userDAOProvider, Provider<RoleDAO> roleDAOProvider,
                        Provider<HttpServletRequest> servletRequestProvider,
                        Provider<HttpServletResponse> servletResponseProvider,
                        Validator validator, @Named("shiro.successUrl") String successURL) {
        this.userDAOProvider = userDAOProvider;
        this.roleDAOProvider = roleDAOProvider;
        this.servletRequestProvider = servletRequestProvider;
        this.servletResponseProvider = servletResponseProvider;
        this.validator = validator;
        this.successURL = successURL;
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

    @POST
    @Path("/loginUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Response loginUser(User user) {
        try {
            /*
              * false = "Do not remember me." A functionality Apache Shiro provides that is like when you enter
              * Amazon.com again. Your username is remembered but before you can do something serious, you have to
              * authenticate again. Makes no sense in an application that is completely unavailable to unauthenticated
              * users!
             */
            SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getLoginName(), user.getPassword(), false));
            User loggedInUser = getLoggedInUser();
            // Check if the user is still active, if not log out again.
            if (!loggedInUser.getActive()) {
                SecurityUtils.getSubject().logout();
                // TODO: Show inactive error somehow
            }
            return createRedirectResponse();
        } catch (AuthenticationException ex) {
            // TODO: Show wrong username / password error somehow!
        } catch (URISyntaxException | MalformedURLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("/registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public void registerUser(User user) {
        user.setActive(false);
        final RoleDAO roleDAO = roleDAOProvider.get();
        user.getRoles().add(roleDAO.findByName(UserRole.USER.getNameInDB()));
        // TODO: Hash password!!
        userDAOProvider.get().create(user);
    }

    public void removeUser(User user) {
        // TODO: Benutzer <-> Rolle Verbindung entfernen
        // TODO: Reservierungen entfernen
    }

    /**
     * Creates a {@link Response} with status code {@link Response.Status#ACCEPTED} which has the "Location" header
     * parameter set with the value to either the previously requested URL (before the successful login) or the
     * "successUrl" bound in the {@link org.grp5.getacar.security.guice.GetACarShiroWebModule}.
     *
     * @return The response to send back
     * @throws MalformedURLException If anything goes wrong while creating the URL
     * @throws URISyntaxException    If anything goes wrong while creating the URL
     */
    private Response createRedirectResponse() throws MalformedURLException, URISyntaxException {
        final HttpServletRequest request = servletRequestProvider.get();
        final SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        URL newURL;
        if (savedRequest != null) {
            String requestURI = savedRequest.getRequestURI();
            newURL = new URL(getBaseURL(request, false) + requestURI);
        } else {
            newURL = new URL(getBaseURL(request, true) + successURL);
        }
        return Response.status(Response.Status.ACCEPTED).location(newURL.toURI()).build();
    }

    /**
     * Constructs the base url from the given {@link HttpServletRequest}.
     * <br/>
     * <br/>
     * For example:
     * <br/>
     * http://localhost:8080/ (includeContextPath = false)
     * <br/>
     * or
     * <br/>
     * http://localhost:8080/getacar (includeContextPath = true)
     *
     * @param request            The request to take the base url from
     * @param includeContextPath Whether to include the context path or not
     * @return The base url in its string representation
     */
    private String getBaseURL(HttpServletRequest request, boolean includeContextPath) {
        String url = request.getRequestURL().toString();
        StringBuilder stringBuilder = new StringBuilder(url.substring(0, url.length()
                - request.getRequestURI().length()));
        if (includeContextPath) {
            stringBuilder.append(request.getContextPath());
        }
        return stringBuilder.toString();
    }

    /**
     * Gs the logged in {@link User}.
     *
     * @return The logged in user or null if the user is not logged in.
     */
    private User getLoggedInUser() {
        return userDAOProvider.get().findByLoginName((String) SecurityUtils.getSubject().getPrincipal());
    }
}