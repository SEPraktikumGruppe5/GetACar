package org.grp5.getacar.test.integration.persistence;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.apache.onami.test.annotation.GuiceModules;
import org.apache.onami.test.annotation.GuiceProvidedModules;
import org.grp5.getacar.persistence.guice.PersistenceModule;
import org.grp5.getacar.service.TimeSimulator;
import org.grp5.getacar.service.TimeSimulatorImpl;
import org.grp5.getacar.test.integration.util.PersistenceInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.TimeZone;

/**
 * Sets up the environment needed to use the {@link org.grp5.getacar.persistence.validation.ValidationHelper}
 */
@GuiceModules({PersistenceModule.class})
public class BaseValidatorIntegrationTest implements Module {
    protected static TimeZone europeBerlinTimeZone = TimeZone.getTimeZone("Europe/Berlin");
    protected static TimeSimulator timeSimulator = new TimeSimulatorImpl();

    @Override
    public void configure(Binder binder) {
        binder.bind(TimeSimulator.class).toInstance(timeSimulator);
    }

    @Inject
    private static PersistenceInitializer persistenceInitializer;

    @GuiceProvidedModules
    public static Module createTestJpaPersistModule() {
        return new JpaPersistModule("GAC_TEST_PU");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        persistenceInitializer.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        persistenceInitializer.stop();
    }
}
