package org.grp5.getacar.persistence.validation;

import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator that checks if either both values are given or none of them.
 */
public class BothOrNoneValidator extends BaseValidator implements ConstraintValidator<BothOrNone, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(BothOrNone constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final Object firstValue = PropertyUtils.getProperty(value, firstFieldName);
            final Object secondValue = PropertyUtils.getProperty(value, secondFieldName);

            if ((firstValue == null && secondValue != null) || firstValue != null && secondValue == null) {
                addConstraintValidation(context, message, firstFieldName);
                addConstraintValidation(context, message, secondFieldName);
                return false;
            }
        } catch (Exception e) {
            // ignore
        }
        return true;
    }
}