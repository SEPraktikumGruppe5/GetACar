package org.grp5.getacar.security.guice;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.grp5.getacar.security.GetACarRealm;

import javax.inject.Named;
import javax.servlet.ServletContext;

/**
 * Application-specific Guice module for Shiro configuration. There is no need to install this module if the
 * {@link SecurityModule} is being installed.
 */
public class GetACarShiroWebModule extends ShiroWebModule {

    public static final Key<PassThruAuthenticationFilter> AUTHC_PASSTHRU = Key.get(PassThruAuthenticationFilter.class);


    public GetACarShiroWebModule(ServletContext servletContext) {
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
        addFilterChain("/app/**", AUTHC);
        addFilterChain("/admin/**", AUTHC);
        addFilterChain("/**", ANON);
        addFilterChain("/rest/**", ANON);
        addFilterChain("/rss/**", ANON);
        addFilterChain("/account/**", AUTHC_PASSTHRU);

        /* Component bindings */
        bind(CredentialsMatcher.class).to(PasswordMatcher.class);
        expose(CredentialsMatcher.class);

        expose(CacheManager.class);

        /* Config constants */
        bindConstant().annotatedWith(Names.named("shiro.loginUrl")).to("/account");
        expose(Key.get(String.class, Names.named("shiro.loginUrl")));
        bindConstant().annotatedWith(Names.named("shiro.successUrl")).to("/app");
        expose(Key.get(String.class, Names.named("shiro.successUrl")));
        bindConstant().annotatedWith(Names.named("shiro.unauthorizedUrl")).to("/error/error.html");
        expose(Key.get(String.class, Names.named("shiro.unauthorizedUrl")));
        bindConstant().annotatedWith(Names.named("shiro.roles.unauthorizedUrl")).to("/error/error.html");
        expose(Key.get(String.class, Names.named("shiro.unauthorizedUrl")));
        bindConstant().annotatedWith(Names.named("shiro.redirectUrl")).to("/account");
        expose(Key.get(String.class, Names.named("shiro.redirectUrl")));
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
