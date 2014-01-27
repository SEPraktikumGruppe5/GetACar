package org.grp5.getacar.resource;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.persist.Transactional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.persistence.dao.RoleDAO;
import org.grp5.getacar.persistence.dao.UserDAO;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.util.UserRole;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.grp5.getacar.resource.form.ChangeUserForm;
import org.grp5.getacar.resource.form.LoginForm;
import org.grp5.getacar.resource.form.RegisterForm;
import org.grp5.getacar.service.TimeSimulator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static javax.ws.rs.core.Response.Status.*;

@Path("/rest/v1/users")
public class UserResource {

    private final Provider<UserDAO> userDAOProvider;
    private final Provider<RoleDAO> roleDAOProvider;
    private final Provider<HttpServletRequest> servletRequestProvider;
    private final Provider<HttpServletResponse> servletResponseProvider;
    private final Provider<PasswordService> passwordServiceProvider;
    private final Provider<TimeSimulator> timeSimulatorProvider;
    private final ValidationHelper validationHelper;
    private final String successURL;

    @Inject
    public UserResource(Provider<UserDAO> userDAOProvider, Provider<RoleDAO> roleDAOProvider,
                        Provider<HttpServletRequest> servletRequestProvider,
                        Provider<HttpServletResponse> servletResponseProvider,
                        Provider<PasswordService> passwordServiceProvider,
                        Provider<TimeSimulator> timeSimulatorProvider,
                        ValidationHelper validationHelper, @Named("shiro.successUrl") String successURL) {
        this.userDAOProvider = userDAOProvider;
        this.roleDAOProvider = roleDAOProvider;
        this.servletRequestProvider = servletRequestProvider;
        this.servletResponseProvider = servletResponseProvider;
        this.passwordServiceProvider = passwordServiceProvider;
        this.timeSimulatorProvider = timeSimulatorProvider;
        this.validationHelper = validationHelper;
        this.successURL = successURL;
    }

    @POST
    @Path("/change")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresRoles("Admin")
    @Transactional(rollbackOn = {Exception.class})
    public Response changeUser(ChangeUserForm changeUserForm) {
        final User user = changeUserForm.getUser();
        final UserDAO userDAO = userDAOProvider.get();
        // needed for some password-specific checks
        final User oldStateUser = userDAO.findCurrentDbStateInstance(user.getId());

        // set old passwords if passwords have not been changed
        if (Strings.isNullOrEmpty(user.getPassword())) {
            user.setPassword(oldStateUser.getPassword());
        }

        if (Strings.isNullOrEmpty(user.getPasswordRepeat())) {
            user.setPasswordRepeat(oldStateUser.getPassword());
        }

        //  validate the input
        validationHelper.validateAndThrow(changeUserForm);

        // encrypt if password changed and validation passed
        if (!oldStateUser.getPassword().equals(user.getPassword())) {
            user.setPassword(encryptPassword(user.getPassword()));
            user.setPasswordRepeat(user.getPassword());
        }

        userDAO.change(user);

        return Response.status(OK).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresAuthentication
    public Response getUser(@PathParam("id") Integer id) {
        final UserDAO userDAO = userDAOProvider.get();
        final User user = userDAO.find(id);

        // user exists
        if (user == null) {
            return Response.status(NOT_FOUND).build();
        }

        return Response.status(OK).entity(Collections.singletonMap("user", user)).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresRoles("Admin")
    public Response getUsers() {
        final UserDAO userDAO = userDAOProvider.get();
        final List<User> users = userDAO.findAll();
        return Response.status(OK).entity(Collections.singletonMap("users", users)).build();
    }

    @POST
    @Path("/loginUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Response loginUser(LoginForm loginForm) {
        try {
            //  validate the input
            validationHelper.validateAndThrow(loginForm);
            /*
              * false = "Do not remember me." A functionality Apache Shiro provides that is like when you enter
              * Amazon.com again. Your username is remembered but before you can do something serious, you have to
              * authenticate again. Makes no sense in an application that is completely unavailable to unauthenticated
              * users!
             */
            SecurityUtils.getSubject().login(new UsernamePasswordToken(loginForm.getLoginName(),
                    loginForm.getPassword(), false));
            User loggedInUser = getLoggedInUser();
            // Check if the user is still active, if not log out again.
            if (!loggedInUser.getActive()) {
                SecurityUtils.getSubject().logout();
                return Response.status(INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("error", "Inactive")) // TODO: il8n messages?
                        .build();
            }
            timeSimulatorProvider.get().setTime(loginForm.getTimeSimulation());
            return createRedirectResponse();
        } catch (AuthenticationException ex) {
            return Response.status(INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("error",
                    "Wrong username or password")).build();
        } catch (URISyntaxException | MalformedURLException e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    public Response registerUser(RegisterForm registerForm) {
        final UserDAO userDAO = userDAOProvider.get();
        final User user = registerForm.getUser();
        user.setActive(false);
        final RoleDAO roleDAO = roleDAOProvider.get();
        user.getRoles().clear();
        user.getRoles().add(roleDAO.findByName(UserRole.USER.getNameInDB()));
        validationHelper.validateAndThrow(registerForm);
        user.setPassword(encryptPassword(user.getPassword()));
        user.setPasswordRepeat(user.getPassword());
        userDAO.create(user);
        return Response.status(CREATED).build();
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
    public Response createRedirectResponse() throws MalformedURLException, URISyntaxException {
        final HttpServletRequest request = servletRequestProvider.get();
        final SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        URL newURL;
        if (savedRequest != null) {
            String requestURI = savedRequest.getRequestURI();
            newURL = new URL(getBaseURL(request, false) + requestURI);
        } else {
            newURL = new URL(getBaseURL(request, true) + successURL);
        }
        return Response.status(ACCEPTED).location(newURL.toURI()).entity(timeSimulatorProvider.get().getTime()).build();
    }

    /**
     * Encrypts a password using the Apache Shiro {@link PasswordService}.
     *
     * @param password The password to encrypt
     * @return The encrypted password
     */
    private String encryptPassword(String password) {
        if (password == null) {
            return null;
        }
        final PasswordService passwordService = passwordServiceProvider.get();
        return passwordService.encryptPassword(password);
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
     * Gets the logged in {@link User}.
     *
     * @return The logged in user or null if the user is not logged in.
     */
    private User getLoggedInUser() {
        return userDAOProvider.get().findByLoginName((String) SecurityUtils.getSubject().getPrincipal());
    }
}