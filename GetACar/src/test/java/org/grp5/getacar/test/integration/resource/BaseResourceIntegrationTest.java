package org.grp5.getacar.test.integration.resource;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.apache.onami.test.annotation.GuiceModules;
import org.apache.onami.test.annotation.GuiceProvidedModules;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.grp5.getacar.persistence.guice.PersistenceModule;
import org.grp5.getacar.resource.guice.ResourcesModule;
import org.grp5.getacar.service.TimeSimulator;
import org.grp5.getacar.service.TimeSimulatorImpl;
import org.grp5.getacar.test.integration.util.PersistenceInitializer;
import org.grp5.getacar.web.guice.annotation.AbsoluteImagePath;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import java.util.TimeZone;

/**
 * Sets-up, tears-down and rollbacks database changes. Rollback occurs after each test method.
 */
@GuiceModules({PersistenceModule.class, ResourcesModule.class})
public class BaseResourceIntegrationTest extends AbstractShiroTest implements Module {

    protected static TimeZone europeBerlinTimeZone = TimeZone.getTimeZone("Europe/Berlin");
    protected static TimeSimulator timeSimulator = new TimeSimulatorImpl();

    @Override
    public void configure(Binder binder) {
        binder.bind(TimeSimulator.class).toInstance(timeSimulator);
        binder.bind(PasswordService.class).to(DefaultPasswordService.class);
        binder.bindConstant().annotatedWith(Names.named("shiro.successUrl")).to("/fakeSuccessUrl");
        binder.bindConstant().annotatedWith(AbsoluteImagePath.class).to("fakeAbsoluteImagePath");
    }

    @Inject
    private static Injector injector;
    @Inject
    private static PersistenceInitializer persistenceInitializer;
    private static EntityManager entityManager;

    @GuiceProvidedModules
    public static Module createTestJpaPersistModule() {
        return new JpaPersistModule("GAC_TEST_PU");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        persistenceInitializer.start();
        entityManager = injector.getInstance(EntityManager.class);
    }

    @Before
    public void setUpBeforeMethod() throws Exception {
        entityManager.getTransaction().begin();
        timeSimulator.setTime(new DateTime(2013, 12, 10, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
    }

    @After
    public void tearDownAfterMethod() throws Exception {
        entityManager.getTransaction().rollback();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        persistenceInitializer.stop();
    }
}