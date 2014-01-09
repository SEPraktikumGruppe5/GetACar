package org.grp5.getacar.persistence.validation;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that the <code>DateTime</code> is before the other <code>DateTime</code>.
 *
 * @author opn
 */
public class DateTimeBeforeOtherDateTimeValidator extends BaseValidator implements
        ConstraintValidator<DateTimeBeforeOtherDateTime, Object> {

    private String dateFieldName;
    private String otherDateFieldName;
    private String message;

    @Override
    public void initialize(DateTimeBeforeOtherDateTime constraintAnnotation) {
        dateFieldName = constraintAnnotation.date();
        otherDateFieldName = constraintAnnotation.otherDate();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            DateTime date = (DateTime) PropertyUtils.getProperty(value, dateFieldName);
            DateTime otherDate = (DateTime) PropertyUtils.getProperty(value, otherDateFieldName);

            if (date != null && otherDate != null) {
                final DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();
                if (dateTimeComparator.compare(date, otherDate) > 0) {
                    addConstraintValidation(context, message, dateFieldName);
                    addConstraintValidation(context, message, otherDateFieldName);
                    return false;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return true;
    }
}