package org.grp5.getacar.persistence.integration.util;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

/**
 * Initializer for Guice-Persist. Only needed in Test mode, otherwise binding GuiceServlet is enough.
 */
public class PersistenceInitializer {
    @Inject
    public PersistenceInitializer(PersistService service) {
        service.start();
    }
}
