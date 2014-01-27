package org.grp5.getacar.test.integration.util;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

/**
 * Initializer for Guice-Persist. Only needed in Test mode, otherwise binding GuiceServlet is enough.
 */
public class PersistenceInitializer {
    private final PersistService service;

    @Inject
    public PersistenceInitializer(PersistService service) {

        this.service = service;
    }

    public void start() {
        service.start();
    }

    public void stop() {
        service.stop();
    }
}
