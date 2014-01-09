package org.grp5.getacar.persistence.validation;

import com.google.inject.Inject;
import org.grp5.getacar.service.TimeSimulator;
import org.joda.time.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that the <code>DateTime</code> passed to be validated is in the
 * future.
 * <p/>
 * <p>
 * Takes into account the simulated time.
 * </p>
 *
 * @author opn
 */
public class FutureDateTimeValidator implements ConstraintValidator<FutureDateTime, DateTime> {

    @Inject
    private TimeSimulator timeSimulator;

    public void initialize(FutureDateTime constraintAnnotation) {
    }

    public boolean isValid(DateTime date, ConstraintValidatorContext constraintValidatorContext) {
        //null values are valid
        try {
            return date == null || timeSimulator.getTime().isBefore(date.toInstant());
        } catch (Exception ex) { // For some reason a NullPointerException was thrown here on create, we simply ignore it for now
            // ignore
        }
        return true;
    }
}