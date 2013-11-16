package org.grp5.getacar.resource.guice;

import com.google.inject.AbstractModule;
import org.grp5.getacar.resource.ReservationResource;
import org.grp5.getacar.resource.UserResource;
import org.grp5.getacar.resource.VehicleResource;

/**
 * Contains the bindings of the resources.
 */
public class ResourcesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(VehicleResource.class);
        bind(ReservationResource.class);
        bind(UserResource.class);
    }
}
