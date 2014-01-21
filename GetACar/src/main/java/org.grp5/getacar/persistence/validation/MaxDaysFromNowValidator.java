package org.grp5.getacar.persistence.validation;

import com.google.inject.Inject;
import org.grp5.getacar.service.TimeSimulator;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that the <code>DateTime</code> passed to be validated is not more than the passed amount of days in
 * future.
 * <p/>
 * <p>
 * Takes into account the simulated time.
 * </p>
 *
 * @author opn
 */
public class MaxDaysFromNowValidator implements ConstraintValidator<MaxDaysFromNow, DateTime> {

    @Inject
    private TimeSimulator timeSimulator;
    private float maxDaysFromNow;

    public void initialize(MaxDaysFromNow maxDaysFromNow) {
        this.maxDaysFromNow = maxDaysFromNow.value();
    }

    public boolean isValid(DateTime value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            final int daysBetween = Days.daysBetween(timeSimulator.getTime().withTimeAtStartOfDay(),
                    value.withTimeAtStartOfDay()).getDays();
            return daysBetween <= maxDaysFromNow;
        } catch (Exception ex) {
            // ignore
        }
        return true;
    }
}