package org.grp5.getacar.log.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import org.grp5.getacar.log.InvocationLogger;
import org.grp5.getacar.log.LogInvocation;

/**
 * Contains the bindings of logging related classes.
 */
public class LogModule extends AbstractModule {

    @Override
    protected void configure() {
        final InvocationLogger invocationLogger = new InvocationLogger();
        requestInjection(invocationLogger);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(LogInvocation.class), invocationLogger);
    }
}
