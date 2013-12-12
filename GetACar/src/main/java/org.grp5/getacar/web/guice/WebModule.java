package org.grp5.getacar.web.guice;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.grp5.getacar.web.filter.BaseURLRedirectFilter;

/**
 * Contains the bindings of the web related classes like {@link javax.servlet.Servlet}s and
 * {@link javax.servlet.Filter}s that are not doing security related stuff and thus are already bound in the
 * {@link org.grp5.getacar.security.guice.GetACarShiroWebModule}.
 */
public class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        // Every servlet (or filter) is required to be a @Singleton
        bind(BaseURLRedirectFilter.class).in(Singleton.class);
        filter("/").through(BaseURLRedirectFilter.class);
    }
}