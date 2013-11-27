package org.grp5.getacar.service.guice;

import com.google.inject.AbstractModule;
import org.grp5.getacar.service.TimeManager;
import org.grp5.getacar.service.TimeManagerImpl;

/**
 * Contains the bindings of the services.
 */
public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TimeManager.class).to(TimeManagerImpl.class);
    }
}