package org.grp5.getacar.service.guice;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.SessionScoped;
import org.grp5.getacar.service.TimeSimulator;
import org.grp5.getacar.service.TimeSimulatorImpl;

/**
 * Contains the bindings of the services.
 */
public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TimeSimulator.class).to(TimeSimulatorImpl.class).in(SessionScoped.class);
    }
}