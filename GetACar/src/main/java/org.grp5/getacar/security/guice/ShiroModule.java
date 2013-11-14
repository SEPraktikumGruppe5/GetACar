package org.grp5.getacar.security.guice;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.grp5.getacar.security.GetACarRealm;

import javax.inject.Named;
import javax.servlet.ServletContext;

/**
 * Application-specific Guice module for Shiro configuration.
 */
public class ShiroModule extends ShiroWebModule {

    public ShiroModule(ServletContext servletContext) {
        super(servletContext);
    }

    protected void configureShiroWeb() {
        /* custom realm registration */
        bindRealm().to(GetACarRealm.class);

//        bind(AjaxCallAuthenticationFilter.class).in(Singleton.class);
//        try {
//            bindRealm().toConstructor(IniRealm.class.getConstructor(Ini.class));
//        } catch (NoSuchMethodException e) {
//            addError(e);
//        }

        /* Filters */
        addFilterChain("/account/logout", LOGOUT);
        addFilterChain("/resources/**", ANON);
//        addFilterChain("/app/gwtRequest/**", AJAX);
        addFilterChain("/**", AUTHC);
        addFilterChain("/account/**", AUTHC, NO_SESSION_CREATION);
        addFilterChain("/error/**", ANON);

        /* Component bindings */
        bind(CredentialsMatcher.class).to(PasswordMatcher.class);
        expose(CredentialsMatcher.class);

        expose(CacheManager.class);

        /* Config constants */
        bindConstant().annotatedWith(Names.named("shiro.loginUrl")).to("/account/login.jsp");
        bindConstant().annotatedWith(Names.named("shiro.successUrl")).to("/index.html");
        bindConstant().annotatedWith(Names.named("shiro.unauthorizedUrl")).to("/error/error.html");
        bindConstant().annotatedWith(Names.named("shiro.roles.unauthorizedUrl")).to("/error/error.html");
        bindConstant().annotatedWith(Names.named("shiro.redirectUrl")).to("/account/login.jsp");
    }

    @Provides
    @Singleton
    CacheManager getCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:org/grp5/getacar/cache/ehcache.xml");
        return cacheManager;
    }

    @Provides
    @Singleton
    @Inject
    LogoutFilter getLogoutFilter(@Named("shiro.redirectUrl") String redirectUrl) {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl(redirectUrl);
        return logoutFilter;
    }

//    @Provides
//    Ini loadShiroIni() {
//        return Ini.fromResourcePath("classpath:shiro.ini");
//    }
}
