package org.grp5.getacar.listener;

import com.google.common.collect.Lists;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import org.apache.onami.logging.slf4j.Slf4jLoggingModule;
import org.grp5.getacar.persistence.guice.PersistenceModule;
import org.grp5.getacar.log.guice.LogModule;
import org.grp5.getacar.resource.guice.ResourcesModule;
import org.grp5.getacar.security.guice.SecurityModule;
import org.grp5.getacar.service.guice.ServicesModule;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Extends {@link GuiceResteasyBootstrapServletContextListener} to get control over the instantiation of the
 * Guice modules.
 */
public class GetACarGuiceResteasyBootstrapServletContextListener extends
        GuiceResteasyBootstrapServletContextListener {

    @Override
    protected List<Module> getModules(ServletContext context) {
        return Lists.newArrayList(new PersistenceModule(), new ResourcesModule(), new ServicesModule(),
                new SecurityModule(context), new Slf4jLoggingModule(Matchers.any()), new LogModule());
    }
}