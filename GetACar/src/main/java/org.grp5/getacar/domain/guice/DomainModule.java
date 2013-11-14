package org.grp5.getacar.domain.guice;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import org.grp5.getacar.domain.dao.*;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import javax.persistence.EntityManager;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Contains the bindings of domain related classes.
 */
public class DomainModule extends ServletModule {

    @Override
    protected void configureServlets() {
        install(new JpaPersistModule("GAC_PU"));

        /* IMPORTANT: Has to be the first filter */
        filter("/*").through(PersistFilter.class);

        bind(VehicleDAO.class).to(VehicleDAOImpl.class);
        bind(UserDAO.class).to(UserDAOImpl.class);
        bind(ReservationDAO.class).to(ReservationDAOImpl.class);
    }

    @Provides
    @Singleton
    private Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Provides
    @Inject
    protected Session providesHibernateSession(EntityManager entityManager) {
        return (Session) entityManager.getDelegate();
    }

    @Provides
    @Inject
    protected StatelessSession providesStatelessHibernateSession(EntityManager entityManager) {
        return ((Session) entityManager.getDelegate()).getSessionFactory().openStatelessSession(); // Don't forget to close the session afterwards!
    }
}
