package org.grp5.getacar.test.integration.resource;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.persistence.entity.VehicleType;
import org.grp5.getacar.resource.VehicleTypeResource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RunWith(OnamiRunner.class)
public class VehicleTypeResourceIntegrationTest extends BaseResourceIntegrationTest implements Module {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
    }

    @Inject
    private static VehicleTypeResource vehicleTypeResource;

    @BeforeClass
    public static void doBeforeClass() throws Exception {
    }

    @Test
    public void getVehicleTypesTest() {
        final Response response = vehicleTypeResource.getVehicleTypes();
        Map<String, List<VehicleType>> vehicleTypesMap = (Map<String, List<VehicleType>>) response.getEntity();
        final List<VehicleType> vehicles = vehicleTypesMap.get("vehicleTypes");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong number of vehicle types", 3, vehicles.size());
    }

    @Test
    public void getVehicleTypeSuccessTest() {
        final Response response = vehicleTypeResource.getVehicleType(1);
        Map<String, VehicleType> vehicleTypeMap = (Map<String, VehicleType>) response.getEntity();
        final VehicleType vehicleType = vehicleTypeMap.get("vehicleType");
        Assert.assertEquals("Wrong status code", 200, response.getStatus());
        Assert.assertEquals("Wrong or no vehicleType", 1, vehicleType != null ? vehicleType.getId() : -1);
    }

    @Test
    public void getVehicleTypeFailTest() {
        final Response response = vehicleTypeResource.getVehicleType(31337);
        Assert.assertEquals("Wrong status code", 404, response.getStatus());
        Assert.assertNull("Returned entity should be null", response.getEntity());
    }
}