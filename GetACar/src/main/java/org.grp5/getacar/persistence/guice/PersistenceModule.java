package org.grp5.getacar.persistence.guice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import org.grp5.getacar.persistence.dao.*;
import org.grp5.getacar.persistence.validation.InjectingConstraintValidationFactory;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.grp5.getacar.persistence.validation.ValidationHelperImpl;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import javax.persistence.EntityManager;
import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Contains the bindings of persistence related classes.
 */
public class PersistenceModule extends ServletModule {

    @Override
    protected void configureServlets() {
        install(new JpaPersistModule("GAC_PU"));

        /* IMPORTANT: Has to be the first filter */
        filter("/*").through(PersistFilter.class);

        bind(ValidationHelper.class).to(ValidationHelperImpl.class).in(Singleton.class);

        bind(VehicleDAO.class).to(VehicleDAOImpl.class);
        bind(VehicleTypeDAO.class).to(VehicleTypeDAOImpl.class);
        bind(UserDAO.class).to(UserDAOImpl.class);
        bind(ReservationDAO.class).to(ReservationDAOImpl.class);
        bind(RoleDAO.class).to(RoleDAOImpl.class);
    }

    /**
     * Creates and reuses injecting JSR 303 Validator factory.
     *
     * @param injector the injector that will be used for the injection.
     * @return The ValidatorFactory.
     */
    @Provides
    @Singleton
    public ValidatorFactory getValidatorFactory(Injector injector) {
        final Configuration<?> configure = Validation.byDefaultProvider().configure();
        return configure.messageInterpolator(configure.getDefaultMessageInterpolator())
                .constraintValidatorFactory(new InjectingConstraintValidationFactory(injector)).buildValidatorFactory();
    }

    /**
     * Creates and reuses injecting JSR 303 Validator.
     *
     * @param validatorFactory the ValidatorFactory to get the Validator from.
     * @return the Validator.
     */
    @Provides
    @Singleton
    public Validator getValidator(ValidatorFactory validatorFactory) {
        return validatorFactory.getValidator();
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