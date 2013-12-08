package org.grp5.getacar.persistence.validation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.beanutils.PropertyUtils;
import org.grp5.getacar.persistence.User;
import org.grp5.getacar.persistence.dao.UserDAO;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;

/**
 *
 */
public class LoginNameNotExistentValidator implements ConstraintValidator<LoginNameNotExistent, User> {

    @Inject
    private Provider<UserDAO> userDAOProvider;
    @Inject
    private Provider<Session> hibernateSessionProvider;
    @Inject
    private Provider<StatelessSession> hibernateStatelessSessionProvider;

    private String loginNameField;
    private String message;

    @Override
    public void initialize(LoginNameNotExistent constraintAnnotation) {
        loginNameField = constraintAnnotation.loginNameField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {
        try {
            final String loginName = (String) PropertyUtils.getProperty(value, loginNameField);
            ClassMetadata meta = hibernateSessionProvider.get().getSessionFactory().getClassMetadata(value.getClass());
            final StatelessSession statelessSession = hibernateStatelessSessionProvider.get();
            Serializable id = meta.getIdentifier(value, (SessionImplementor) statelessSession);
            statelessSession.close();
            final UserDAO userDAO = userDAOProvider.get();
            final User byLoginName = userDAO.findByLoginName(loginName);
            if (id == null) { // new user
                if (byLoginName != null) {
                    addConstraintValidation(context);
                    return false;
                }
            } else { // user edit
                if (byLoginName != null && !byLoginName.getId().equals(id)) {
                    addConstraintValidation(context);
                    return false;
                }
            }
        } catch (Exception ex) {
            // ignore
        }
        return true;
    }

    private void addConstraintValidation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addNode(loginNameField)
                .addConstraintViolation();
    }
}