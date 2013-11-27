package org.grp5.getacar.security.guice;

import com.google.inject.AbstractModule;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.apache.shiro.guice.web.ShiroWebModule;

import javax.servlet.ServletContext;

/**
 *
 */
public class SecurityModule extends AbstractModule {

    private final ServletContext context;

    public SecurityModule(ServletContext context) {
        this.context = context;
    }

    @Override
    protected void configure() {
        install(new ShiroAopModule());
        install(new GetACarShiroWebModule(context));
        ShiroWebModule.bindGuiceFilter(binder());

        bind(PasswordService.class).to(DefaultPasswordService.class);
    }
}
