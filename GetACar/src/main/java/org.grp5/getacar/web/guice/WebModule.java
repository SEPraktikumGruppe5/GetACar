package org.grp5.getacar.web.guice;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import org.grp5.getacar.web.filter.BaseURLRedirectFilter;
import org.grp5.getacar.web.guice.annotation.AbsoluteImagePath;
import org.grp5.getacar.web.servlet.ImageServeServlet;
import org.grp5.getacar.web.servlet.ImageUploadServlet;
import org.grp5.getacar.web.util.OSUtil;

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
        bind(ImageServeServlet.class).in(Singleton.class);
        serve("/images").with(ImageServeServlet.class);
        serve("/images/*").with(ImageServeServlet.class);
        bind(ImageUploadServlet.class).in(Singleton.class);
        serve("/uploads").with(ImageUploadServlet.class);
        serve("/uploads/*").with(ImageUploadServlet.class);

        /* Constants */
        if (OSUtil.isUnix()) {
            bindConstant().annotatedWith(AbsoluteImagePath.class).to("/opt/GetACarData/images/");
        } else if (OSUtil.isWindows()) {
            bindConstant().annotatedWith(AbsoluteImagePath.class).to("C:\\getacardata\\images\\");
        }
    }
}