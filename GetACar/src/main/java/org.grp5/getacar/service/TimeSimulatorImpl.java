package org.grp5.getacar.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 *
 */
public class TimeSimulatorImpl implements TimeSimulator {

    private final Provider<HttpServletRequest> servletRequestProvider;

    @Inject
    public TimeSimulatorImpl(Provider<HttpServletRequest> servletRequestProvider) {
        this.servletRequestProvider = servletRequestProvider;
    }

    @Override
    public Date getTime() {
        final HttpServletRequest httpServletRequest = servletRequestProvider.get();
        final HttpSession session = httpServletRequest.getSession();
        final Date simulatedTime = (Date) session.getAttribute("simulatedTime");
        return new Date();
    }
}
