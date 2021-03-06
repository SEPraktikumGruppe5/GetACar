package org.grp5.getacar.persistence.validation;

import org.apache.commons.beanutils.PropertyUtils;
import org.grp5.getacar.persistence.entity.BaseEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator that checks if the values of the two password fields match.
 */
public class PasswordsMatchValidator extends BaseValidator implements ConstraintValidator<PasswordsMatch, BaseEntity> {

    private String passwordFieldName;
    private String passwordRepeatFieldName;
    private String message;

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        passwordFieldName = constraintAnnotation.password();
        passwordRepeatFieldName = constraintAnnotation.passwordRepeat();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(BaseEntity value, ConstraintValidatorContext context) {
        try {
            final String password = (String) PropertyUtils.getProperty(value, passwordFieldName);
            final String passwordRepeat = (String) PropertyUtils.getProperty(value, passwordRepeatFieldName);

            if (!password.equals(passwordRepeat)) {
                addConstraintValidation(context, message, passwordFieldName);
                addConstraintValidation(context, message, passwordRepeatFieldName);
                return false;
            }
        } catch (Exception e) {
            addConstraintValidation(context, message, passwordFieldName);
            addConstraintValidation(context, message, passwordRepeatFieldName);
            return false;
        }
        return true;
    }
}