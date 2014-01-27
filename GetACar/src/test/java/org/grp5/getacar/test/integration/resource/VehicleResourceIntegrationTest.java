package org.grp5.getacar.test.integration.resource;

import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.onami.test.OnamiRunner;
import org.codehaus.jackson.map.ObjectMapper;
import org.grp5.getacar.persistence.dao.VehicleDAO;
import org.grp5.getacar.persistence.dao.VehicleTypeDAO;
import org.grp5.getacar.persistence.dto.VehicleSearchResult;
import org.grp5.getacar.persistence.dto.VehicleSearchResults;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.entity.VehicleImage;
import org.grp5.getacar.persistence.entity.VehicleType;
import org.grp5.getacar.resource.ReservationResource;
import org.grp5.getacar.resource.VehicleResource;
import org.grp5.getacar.resource.form.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@RunWith(OnamiRunner.class)
public class VehicleResourceIntegrationTest extends BaseResourceIntegrationTest implements Module {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
    }

    @Inject
    private static VehicleResource vehicleResource;
    @Inject
    private static VehicleDAO vehicleDAO;
    @Inject
    private static VehicleTypeDAO vehicleTypeDAO;
    @Inject
    private static ReservationResource reservationResource;

    @BeforeClass
    public static void doBeforeClass() throws Exception {
        vehicleResource = Mockito.spy(vehicleResource);
        Mockito.doReturn(true).when(vehicleResource).imageExists("vehicle1.png");
        Mockito.doReturn(true).when(vehicleResource).imageExists("testvehicle.png");

        reservationResource = Mockito.spy(reservationResource);
        final User adminUser = new User();
        adminUser.setId(1);
        Mockito.doReturn(adminUser).when(reservationResource).getLoggedInUser();
    }

    @Test
    public void addVehicleSuccessTest() {
        final CreateVehicleForm createVehicleForm = new CreateVehicleForm();

        final int count = vehicleDAO.count();

        final Vehicle vehicle = new Vehicle();
        vehicle.setLicenseNumber("C-KK 777");
        vehicle.setInitialLongitude(new BigDecimal(50));
        vehicle.setInitialLatitude(new BigDecimal(12));
        vehicle.setActive(true);
        vehicle.setComment("TestVehicle");
        vehicle.setVehicleType(vehicleTypeDAO.find(1));
        final VehicleImage vehicleImage = new VehicleImage();
        vehicleImage.setVehicle(vehicle);
        vehicleImage.setFileName("testvehicle.png");
        vehicle.getVehicleImages().add(vehicleImage);

        createVehicleForm.setVehicle(vehicle);

        final Response response = vehicleResource.addVehicle(createVehicleForm);

        Assert.assertEquals("Wrong response status code", 201, response.getStatus());
        Assert.assertEquals("Wrong vehicle count", count + 1, vehicleDAO.count());
    }

    // TODO: addVehicleFailTest(s) ?

    @Test
    public void changeVehicleSuccessTest() throws IOException {
        final ChangeVehicleForm changeVehicleForm = new ChangeVehicleForm();

        final Vehicle vehicle1 = vehicleDAO.find(1);

        vehicle1.setLicenseNumber("C-KK 777");
        vehicle1.setInitialLongitude(new BigDecimal(50));
        vehicle1.setInitialLatitude(new BigDecimal(12));
        vehicle1.setActive(false);
        vehicle1.setComment("TestVehicle");
        vehicle1.setVehicleType(vehicleTypeDAO.find(3));
        final VehicleImage vehicleImage = new VehicleImage();
        vehicleImage.setVehicle(vehicle1);
        vehicleImage.setFileName("testvehicle.png");
        vehicle1.getVehicleImages().add(vehicleImage);

        changeVehicleForm.setVehicle(vehicle1);

        final Vehicle vehicle1BeforeSaveChanges = deepCopy(vehicle1);

        final Response response = vehicleResource.changeVehicle(changeVehicleForm);

        final Vehicle vehicle1AfterSaveChanges = vehicleDAO.find(1);

        Assert.assertEquals("Wrong response status code", 200, response.getStatus());
        Assert.assertTrue("Some changes not saved",
                vehicle1BeforeSaveChanges.getLicenseNumber()
                        .equals(vehicle1AfterSaveChanges.getLicenseNumber()) &&
                        vehicle1BeforeSaveChanges.getInitialLongitude()
                                .equals(vehicle1AfterSaveChanges.getInitialLongitude()) &&
                        vehicle1BeforeSaveChanges.getInitialLatitude()
                                .equals(vehicle1AfterSaveChanges.getInitialLatitude()) &&
                        vehicle1BeforeSaveChanges.getActive()
                                .equals(vehicle1AfterSaveChanges.getActive()) &&
                        vehicle1BeforeSaveChanges.getComment()
                                .equals(vehicle1AfterSaveChanges.getComment()) &&
                        vehicle1BeforeSaveChanges.getVehicleType()
                                .equals(vehicle1AfterSaveChanges.getVehicleType()) &&
                        vehicle1BeforeSaveChanges.getVehicleImages().size() ==
                                vehicle1AfterSaveChanges.getVehicleImages().size());
    }

    // TODO: changeVehicleFailTest(s) ?

    @Test
    public void getVehiclesTest() {
        final Response response = vehicleResource.getVehicles();
        Map<String, List<Vehicle>> vehiclesMap = (Map<String, List<Vehicle>>) response.getEntity();
        final List<Vehicle> vehicles = vehiclesMap.get("vehicles");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong number of vehicles", 8, vehicles.size());
    }

    @Test
    public void getVehicleSuccessTest() {
        final Response response = vehicleResource.getVehicle(1);
        Map<String, Vehicle> vehicleMap = (Map<String, Vehicle>) response.getEntity();
        final Vehicle vehicle = vehicleMap.get("vehicle");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong or no vehicle", 1, vehicle != null ? vehicle.getId() : -1);
    }

    @Test
    public void getVehicleFailTest() {
        final Response response = vehicleResource.getVehicle(31337);
        Assert.assertEquals("Wrong status code", 404, response.getStatus());
        Assert.assertNull("Returned entity should be null", response.getEntity());
    }

    @Test
    public void searchVehiclesMinimumParametersSuccessTest() {
        final SearchVehiclesForm searchVehiclesForm = new SearchVehiclesForm();

        final Position position = new Position();
        position.setLatitude(new BigDecimal(50.8428159));
        position.setLongitude(new BigDecimal(12.89787739999997));
        searchVehiclesForm.setPosition(position);
        searchVehiclesForm.setRadius(2);

        final Response response = vehicleResource.searchVehicles(searchVehiclesForm);
        final VehicleSearchResults vehicleSearchResults = (VehicleSearchResults) response.getEntity();
        final List<VehicleSearchResult> vehicleSearchResultList = vehicleSearchResults.getVehicleSearchResults();
        final SearchVehiclesForm searchParameters = vehicleSearchResults.getSearchParameters();

        Assert.assertEquals("Wrong number of search results", 2, vehicleSearchResultList.size());
        Assert.assertEquals("Wrong radius in search results parameters", (int) searchVehiclesForm.getRadius(),
                (int) searchParameters.getRadius());
        Assert.assertEquals("Wrong position in search results parameters", position,
                searchParameters.getPosition());
        Assert.assertNull("Search results parameters start time should be null", searchParameters.getStartTime());
        Assert.assertNull("Search results parameters end time should be null", searchParameters.getEndTime());
        Assert.assertNull("Search results parameters vehicle type should be null", searchParameters.getVehicleType());
    }

    @Test
    public void searchVehiclesAllParametersSuccessTest() {
        final SearchVehiclesForm searchVehiclesForm = new SearchVehiclesForm();

        final Position position = new Position();
        position.setLatitude(new BigDecimal(50.8428159));
        position.setLongitude(new BigDecimal(12.89787739999997));
        searchVehiclesForm.setPosition(position);
        searchVehiclesForm.setRadius(2);
        final VehicleType carVehicleType = new VehicleType();
        carVehicleType.setId(1); // Only find cars
        searchVehiclesForm.setVehicleType(carVehicleType);
        searchVehiclesForm.setStartTime(new DateTime(2013, 12, 11, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        searchVehiclesForm.setEndTime(new DateTime(2013, 12, 13, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));

        final Response response = vehicleResource.searchVehicles(searchVehiclesForm);
        final VehicleSearchResults vehicleSearchResults = (VehicleSearchResults) response.getEntity();
        final List<VehicleSearchResult> vehicleSearchResultList = vehicleSearchResults.getVehicleSearchResults();
        final SearchVehiclesForm searchParameters = vehicleSearchResults.getSearchParameters();

        Assert.assertEquals("Wrong number of search results", 1, vehicleSearchResultList.size()); // --> only cars: one result
        Assert.assertEquals("Wrong radius in search results parameters", (int) searchVehiclesForm.getRadius(),
                (int) searchParameters.getRadius());
        Assert.assertEquals("Wrong position in search results parameters", position,
                searchParameters.getPosition());
        Assert.assertEquals("Wrong search results parameters start time ", searchVehiclesForm.getStartTime(),
                searchParameters.getStartTime());
        Assert.assertEquals("Wrong search results parameters end time ", searchVehiclesForm.getEndTime(),
                searchParameters.getEndTime());
        Assert.assertEquals("Wrong search results parameters vehicle type ", searchVehiclesForm.getVehicleType(),
                searchParameters.getVehicleType());
    }

    @Test
    public void searchVehiclesMissingParametersFailTest() {
        final SearchVehiclesForm searchVehiclesForm = new SearchVehiclesForm();

        Map<String, Object> violatedPathsAndAnnotationClasses = Maps.newHashMap();
        try {
            vehicleResource.searchVehicles(searchVehiclesForm);
        } catch (ConstraintViolationException cve) {
            final Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violatedPathsAndAnnotationClasses.put(constraintViolation.getPropertyPath().toString(),
                        (constraintViolation).getConstraintDescriptor().getAnnotation().annotationType());
            }
        }

        Assert.assertEquals("Wrong number of violations", 3, violatedPathsAndAnnotationClasses.size());
        Assert.assertTrue("Missing expected violation", (violatedPathsAndAnnotationClasses.containsKey("radius") &&
                violatedPathsAndAnnotationClasses.get("radius").equals(NotNull.class)));
        Assert.assertTrue("Missing expected violation", (violatedPathsAndAnnotationClasses.containsKey("position.longitude") &&
                violatedPathsAndAnnotationClasses.get("position.longitude").equals(NotNull.class)));
        Assert.assertTrue("Missing expected violation", (violatedPathsAndAnnotationClasses.containsKey("position.latitude") &&
                violatedPathsAndAnnotationClasses.get("position.latitude").equals(NotNull.class)));
    }

    @Test
    public void searchVehicleAfterReservationTest() {
        final SearchVehiclesForm searchVehiclesForm = new SearchVehiclesForm();

        final Position position = new Position();
        position.setLatitude(new BigDecimal(50.8428159));
        position.setLongitude(new BigDecimal(12.89787739999997));
        searchVehiclesForm.setPosition(position);
        searchVehiclesForm.setRadius(2);

        final Response firstSearchResponse = vehicleResource.searchVehicles(searchVehiclesForm);
        final VehicleSearchResults firstVehicleSearchResults = (VehicleSearchResults) firstSearchResponse.getEntity();
        final List<VehicleSearchResult> firstVehicleSearchResultList = firstVehicleSearchResults.getVehicleSearchResults();

        Assert.assertEquals("Wrong number of search results before reservation", 2, firstVehicleSearchResultList.size());

        final ReserveVehicleForm reserveVehicleForm = new ReserveVehicleForm();

        final Vehicle vehicle = new Vehicle();
        vehicle.setId(6);
        reserveVehicleForm.setVehicle(vehicle);
        reserveVehicleForm.setStartTime(new DateTime(2013, 12, 11, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        reserveVehicleForm.setEndTime(new DateTime(2013, 12, 13, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        reserveVehicleForm.setStartPosition(position);
        reserveVehicleForm.setEndPosition(position);

        final Response reserveResponse = reservationResource.reserveVehicle(reserveVehicleForm);

        Assert.assertEquals("Wrong reservation response status code", 201, reserveResponse.getStatus());

        final Response secondResponse = vehicleResource.searchVehicles(searchVehiclesForm);
        final VehicleSearchResults secondVehicleSearchResults = (VehicleSearchResults) secondResponse.getEntity();
        final List<VehicleSearchResult> secondVehicleSearchResultList = secondVehicleSearchResults.getVehicleSearchResults();
        Assert.assertEquals("Wrong number of search results after reservation", 1, secondVehicleSearchResultList.size());
    }

    private Vehicle deepCopy(Vehicle vehicle) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mapper.writeValue(byteArrayOutputStream, vehicle);
        byteArrayOutputStream.close();
        final String vehicleJson = byteArrayOutputStream.toString();

        return mapper.readValue(vehicleJson, Vehicle.class);
    }
}