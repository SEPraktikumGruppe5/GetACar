package org.grp5.getacar.resource.guice;

import com.google.inject.AbstractModule;
import org.grp5.getacar.resource.*;

/**
 * Contains the bindings of the resources.
 */
public class ResourcesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ReservationResource.class);
        bind(TimeResource.class);
        bind(UserResource.class);
        bind(VehicleResource.class);
        bind(VehicleTypeResource.class);
    }
}