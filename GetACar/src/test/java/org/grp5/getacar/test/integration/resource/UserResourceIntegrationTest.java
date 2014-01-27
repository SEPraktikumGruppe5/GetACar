package org.grp5.getacar.test.integration.resource;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.onami.test.OnamiRunner;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.codehaus.jackson.map.ObjectMapper;
import org.grp5.getacar.persistence.dao.RoleDAO;
import org.grp5.getacar.persistence.dao.UserDAO;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.resource.UserResource;
import org.grp5.getacar.resource.form.ChangeUserForm;
import org.grp5.getacar.resource.form.LoginForm;
import org.grp5.getacar.resource.form.RegisterForm;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.ACCEPTED;

/**
 *
 */
@RunWith(OnamiRunner.class)
public class UserResourceIntegrationTest extends BaseResourceIntegrationTest implements Module {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
    }

    @Inject
    private static UserResource userResource;
    @Inject
    private static UserDAO userDAO;
    @Inject
    private static RoleDAO roleDAO;

    @BeforeClass
    public static void doBeforeClass() throws Exception {

    }

    @Test
    public void changeUserSuccessTest() throws IOException {
        ChangeUserForm changeUserForm = new ChangeUserForm();

        final User user1 = userDAO.find(1);
        user1.setPassword("passwort123");
        user1.setPasswordRepeat("passwort123");
        user1.setLoginName("NoMoreAdmin");
        user1.setActive(false);
        user1.setEmail("degraded@getacar.com");
        user1.setFirstName("SoSad");
        user1.setLastName("IWasAdmin");
        user1.getRoles().remove(roleDAO.findByName("Admin"));

        final User user1BeforeSaveChanges = deepCopy(user1);
        user1BeforeSaveChanges.setPassword(user1.getPassword()); // Jackson ignores that for security reasons usually ...

        changeUserForm.setUser(user1);
        final Response response = userResource.changeUser(changeUserForm);

        final User user1AfterSaveChanges = userDAO.find(1);

        Assert.assertEquals("Wrong response status code", 200, response.getStatus());
        Assert.assertTrue("Not all changes saved",
                user1BeforeSaveChanges.getLoginName().equals(user1AfterSaveChanges.getLoginName()) &&
                        user1BeforeSaveChanges.getActive().equals(user1AfterSaveChanges.getActive()) &&
                        user1BeforeSaveChanges.getRoles().equals(user1AfterSaveChanges.getRoles()) &&
                        user1BeforeSaveChanges.getEmail().equals(user1AfterSaveChanges.getEmail()) &&
                        user1BeforeSaveChanges.getFirstName().equals(user1AfterSaveChanges.getFirstName()) &&
                        user1BeforeSaveChanges.getLastName().equals(user1AfterSaveChanges.getLastName()) &&
                        user1BeforeSaveChanges.getLoginName().equals(user1AfterSaveChanges.getLoginName()) &&
                        !user1BeforeSaveChanges.getPassword().equals(user1AfterSaveChanges.getPassword()));
    }

    @Test
    public void getUsersTest() {
        final Response response = userResource.getUsers();
        Map<String, List<User>> usersMap = (Map<String, List<User>>) response.getEntity();
        final List<User> vehicles = usersMap.get("users");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong number of users", 3, vehicles.size());
    }

    @Test
    public void getUserSuccessTest() {
        final Response response = userResource.getUser(1);
        Map<String, User> userMap = (Map<String, User>) response.getEntity();
        final User user = userMap.get("user");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong or no user", 1, user != null ? user.getId() : -1);
    }

    @Test
    public void getUserFailTest() {
        final Response response = userResource.getUser(31337);
        Assert.assertEquals("Wrong status code", 404, response.getStatus());
        Assert.assertNull("Returned entity should be null", response.getEntity());
    }

    @Test
    public void loginSuccessTest() throws MalformedURLException, URISyntaxException {
        final UserResource userResource = Mockito.spy(UserResourceIntegrationTest.userResource);
        final URI location = URI.create("http://test.com");
        Mockito.doReturn(Response.status(ACCEPTED).location(location).entity(new DateTime())
                .build()).when(userResource).createRedirectResponse();
        Subject subjectUnderTest = Mockito.spy(new DelegatingSubject(Mockito.mock(org.apache.shiro.mgt.SecurityManager.class)));
        Mockito.doReturn(true).when(subjectUnderTest).isAuthenticated();
        Mockito.doReturn("admin").when(subjectUnderTest).getPrincipal();
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(subjectUnderTest).login((AuthenticationToken) Mockito.any());
        setSubject(subjectUnderTest);

        final LoginForm loginForm = new LoginForm();
        loginForm.setLoginName("admin");
        loginForm.setPassword("admin");
        loginForm.setTimeSimulation(new DateTime());

        final Response response = userResource.loginUser(loginForm);

        Assert.assertEquals("Wrong response status code", 202, response.getStatus());
        Assert.assertEquals("Location header missing", location, response.getLocation());
    }

    @Test
    public void registerSuccessTest() {
        final RegisterForm registerForm = new RegisterForm();

        final int count = userDAO.count();

        final User user = new User();
        user.setLoginName("newuser");
        user.setFirstName("newuser");
        user.setLastName("newuser");
        user.setPassword("newuser");
        user.setPasswordRepeat("newuser");
        user.setEmail("newuser@getacar.com");
        user.setActive(true);

        registerForm.setUser(user);

        Response response;
        try {
            userResource.registerUser(registerForm);
        } catch (ConstraintViolationException cve) {
            Assert.assertEquals("Wrong number of constraint violations", 1, cve.getConstraintViolations().size());
        }
        registerForm.setAcceptTOS(true);

        response = userResource.registerUser(registerForm);

        Assert.assertEquals("Wrong response status code", 201, response.getStatus());
        Assert.assertEquals("Wrong number of total users", count + 1, userDAO.count());
    }

    private User deepCopy(User user) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mapper.writeValue(byteArrayOutputStream, user);
        byteArrayOutputStream.close();
        final String userJson = byteArrayOutputStream.toString();

        return mapper.readValue(userJson, User.class);
    }
}