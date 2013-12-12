package org.grp5.getacar.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that redirects from the base url to the main app (MUST be applied to the '/' path for that to work, as its
 * implementation is kind of dumb).
 */
public class BaseURLRedirectFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //  nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
            IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.sendRedirect("app/");
    }

    @Override
    public void destroy() {
        //  nothing to do
    }
}
