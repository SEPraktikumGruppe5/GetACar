package org.grp5.getacar.resource;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.grp5.getacar.log.LogInvocation;
import org.grp5.getacar.persistence.dao.UserDAO;
import org.grp5.getacar.persistence.dao.VehicleDAO;
import org.grp5.getacar.persistence.dao.VehicleImageDAO;
import org.grp5.getacar.persistence.dto.VehicleSearchResult;
import org.grp5.getacar.persistence.dto.VehicleSearchResults;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.entity.VehicleImage;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.grp5.getacar.resource.form.ChangeVehicleForm;
import org.grp5.getacar.resource.form.CreateVehicleForm;
import org.grp5.getacar.resource.form.Position;
import org.grp5.getacar.resource.form.SearchVehiclesForm;
import org.grp5.getacar.service.RSSFeed;
import org.grp5.getacar.service.TimeSimulator;
import org.grp5.getacar.service.util.FeedMessage;
import org.grp5.getacar.web.guice.annotation.AbsoluteImagePath;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path("/rest/v1/vehicles")
public class VehicleResource {

    private final String absoluteImagePath;
    private final Provider<VehicleDAO> vehicleDAOProvider;
    private final Provider<VehicleImageDAO> vehicleImageDAOProvider;
    private final Provider<UserDAO> userDAOProvider;
    private final Provider<TimeSimulator> timeSimulatorProvider;
    private final ValidationHelper validationHelper;
    private final RSSFeed rssFeed;

    @Inject
    public VehicleResource(@AbsoluteImagePath String absoluteImagePath, Provider<VehicleDAO> vehicleDAOProvider,
                           Provider<VehicleImageDAO> vehicleImageDAOProvider,
                           Provider<UserDAO> userDAOProvider, Provider<TimeSimulator> timeSimulatorProvider,
                           ValidationHelper validationHelper, RSSFeed rssFeed) {
        this.absoluteImagePath = absoluteImagePath;
        this.vehicleDAOProvider = vehicleDAOProvider;
        this.vehicleImageDAOProvider = vehicleImageDAOProvider;
        this.userDAOProvider = userDAOProvider;
        this.timeSimulatorProvider = timeSimulatorProvider;
        this.validationHelper = validationHelper;
        this.rssFeed = rssFeed;
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresRoles("Admin")
    @Transactional(rollbackOn = {Exception.class})
    public Response addVehicle(CreateVehicleForm createVehicleForm) throws Exception {
        //  validate the input
        validationHelper.validateAndThrow(createVehicleForm);

        final VehicleDAO vehicleDAO = vehicleDAOProvider.get();
        final Vehicle vehicle = createVehicleForm.getVehicle();

        for (Iterator<VehicleImage> iterator = vehicle.getVehicleImages().iterator(); iterator.hasNext(); ) {
            VehicleImage vehicleImage = iterator.next();
            if (!imageExists(vehicleImage.getFileName())) {
                iterator.remove();
            } else {
                vehicleImage.setVehicle(vehicle);
            }
        }

        vehicleDAO.create(vehicle);

        addFeedMessage(vehicle);

        return Response.status(CREATED).build();
    }

    public void addFeedMessage(Vehicle vehicle) throws Exception {
        rssFeed.addFeedMessage(createFeedMessage(vehicle));
    }

    private FeedMessage createFeedMessage(Vehicle vehicle) {
        final User loggedInUser = getLoggedInUser();
        final FeedMessage feedMessage = new FeedMessage();
        feedMessage.setAuthor(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
        feedMessage.setDescription("A new " + vehicle.getVehicleType().getName() +
                " has been added!<br/><br/>" +
                "Description:<br/>" +
                vehicle.getComment());
        feedMessage.setGuid(vehicle.getLicenseNumber());
        feedMessage.setTitle("A shiny new vehicle is available");
        feedMessage.setLink("http://localhost:8080/getacar/app/#/vehicles/" + String.valueOf(vehicle.getId()));
        return feedMessage;
    }

    @POST
    @Path("/change")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresRoles("Admin")
    @Transactional(rollbackOn = {Exception.class})
    public Response changeVehicle(ChangeVehicleForm changeVehicleForm) {
        //  validate the input
        validationHelper.validateAndThrow(changeVehicleForm);

        final VehicleDAO vehicleDAO = vehicleDAOProvider.get();
        final Vehicle vehicle = changeVehicleForm.getVehicle();
        final Vehicle oldVehicle = vehicleDAO.find(vehicle.getId());
        final List<VehicleImage> oldVehicleVehicleImages = oldVehicle.getVehicleImages();
        List<VehicleImage> imagesToDelete = Lists.newArrayList();
        for (Iterator<VehicleImage> iterator = vehicle.getVehicleImages().iterator(); iterator.hasNext(); ) {
            VehicleImage vehicleImage = iterator.next();
            if (!imageExists(vehicleImage.getFileName())) {
                imagesToDelete.add(vehicleImage);
                iterator.remove();
            } else {
                vehicleImage.setVehicle(vehicle);
            }
        }
        for (Iterator<VehicleImage> iterator = oldVehicleVehicleImages.iterator(); iterator.hasNext(); ) {
            VehicleImage vehicleImage = iterator.next();
            if (!vehicle.getVehicleImages().contains(vehicleImage)) {
                imagesToDelete.add(vehicleImage);
                vehicleImage.setVehicle(null);
                iterator.remove();
            }
        }

        final VehicleImageDAO vehicleImageDAO = vehicleImageDAOProvider.get();
        for (VehicleImage vehicleImage : imagesToDelete) {
            if (vehicleImage.getId() != null) {
                vehicleImageDAO.remove(vehicleImage);
                if (imageExists(vehicleImage.getFileName())) {
                    new File(vehicleImage.getFileName()).delete();
                }
            }
        }

        vehicleDAO.change(vehicle);

        return Response.status(OK).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresAuthentication
    public Response getVehicle(@PathParam("id") Integer id) {
        final VehicleDAO vehicleDAO = vehicleDAOProvider.get();
        final Vehicle vehicle = vehicleDAO.find(id);

        // vehicle exists?
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(OK).entity(Collections.singletonMap("vehicle", vehicle)).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresRoles("Admin")
    @LogInvocation
    public Response getVehicles() {
        final VehicleDAO vehicleDAO = vehicleDAOProvider.get();
        final List<Vehicle> vehicles = vehicleDAO.findAll();
        return Response.status(OK).entity(Collections.singletonMap("vehicles", vehicles)).build();
    }

    public void removeVehicle(Vehicle vehicle) {

    }

    @POST
    @Path("/searchVehicles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @LogInvocation
    @RequiresAuthentication
//    @ValidateAndThrow TODO: Impl. and use everywhere where form validation is done
    public Response searchVehicles(SearchVehiclesForm searchVehiclesForm) {
        validationHelper.validateAndThrow(searchVehiclesForm); // TODO: Do this by AOP!?

        final VehicleDAO vehicleDAO = vehicleDAOProvider.get();
        final Position position = searchVehiclesForm.getPosition();
        final List<VehicleSearchResult> vehicleSearchResultList = vehicleDAO.find(position.getLatitude(),
                position.getLongitude(), searchVehiclesForm.getRadius(), searchVehiclesForm.getVehicleType(),
                searchVehiclesForm.getStartTime(), searchVehiclesForm.getEndTime(),
                timeSimulatorProvider.get().getTime());
        final VehicleSearchResults vehicleSearchResults = new VehicleSearchResults();
        vehicleSearchResults.setSearchParameters(searchVehiclesForm);
        vehicleSearchResults.setVehicleSearchResults(vehicleSearchResultList);

        return Response.status(OK).entity(vehicleSearchResults).build();
    }

    public boolean imageExists(String imageName) {
        if (Strings.isNullOrEmpty(imageName)) {
            return false;
        }
        final File file = new File(absoluteImagePath + imageName);
        return file.exists();
    }

    /**
     * Gets the logged in {@link org.grp5.getacar.persistence.entity.User}.
     *
     * @return The logged in user or null if the user is not logged in.
     */
    private User getLoggedInUser() {
        return userDAOProvider.get().findByLoginName((String) SecurityUtils.getSubject().getPrincipal());
    }
}
