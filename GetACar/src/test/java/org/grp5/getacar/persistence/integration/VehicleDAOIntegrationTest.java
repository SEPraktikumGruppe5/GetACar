package org.grp5.getacar.persistence.integration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.grp5.getacar.persistence.dao.VehicleDAO;
import org.grp5.getacar.persistence.dto.VehicleSearchResult;
import org.grp5.getacar.persistence.entity.VehicleType;
import org.grp5.getacar.persistence.guice.PersistenceModule;
import org.grp5.getacar.persistence.integration.util.PersistenceInitializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.*;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.TimeZone;

/**
 *
 */
public class VehicleDAOIntegrationTest {
    static Injector injector;
    static EntityManager entityManager;
    static VehicleDAO vehicleDAO;

    @BeforeClass
    public static void beforeClass() {
        injector = Guice.createInjector(new PersistenceModule());
        injector.getInstance(PersistenceInitializer.class);
        entityManager = injector.getInstance(EntityManager.class);
        vehicleDAO = injector.getInstance(VehicleDAO.class);
    }

    @Before
    public void setUp() throws Exception {
        entityManager.getTransaction().begin();
    }

    @Test
    public void findVehiclesTest() {
        final VehicleType vehicleType = new VehicleType();
        vehicleType.setId(1);
        final List<VehicleSearchResult> vehicleSearchResults = vehicleDAO.find(new BigDecimal(50), new BigDecimal(12),
                100000, vehicleType, null, null, new DateTime(2013, 12, 10, 10, 0, DateTimeZone.forTimeZone(TimeZone.getDefault())));
        Assert.assertEquals(1, vehicleSearchResults.size());
    }

    @After
    public void tearDown() throws Exception {
        entityManager.getTransaction().rollback();
    }
}