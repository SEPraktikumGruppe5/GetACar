package org.grp5.getacar.persistence.validation;

import javax.validation.ConstraintValidatorContext;

/**
 *
 */
public abstract class BaseValidator {

    protected void addConstraintValidation(ConstraintValidatorContext context, String message, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addNode(field)
                .addConstraintViolation();
    }
}
