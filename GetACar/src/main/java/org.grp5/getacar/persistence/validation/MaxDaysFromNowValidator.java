package org.grp5.getacar.persistence.validation;

import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxDaysFromNowValidator implements ConstraintValidator<MaxDaysFromNow, DateTime> {

	private float maxDaysFromNow;

	public void initialize(MaxDaysFromNow maxDaysFromNow) {
		this.maxDaysFromNow = maxDaysFromNow.value();
	}

	public boolean isValid(DateTime value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            final int daysBetween = Days.daysBetween(new DateTime().withTimeAtStartOfDay(),
                    value.withTimeAtStartOfDay()).getDays();
            return daysBetween <= maxDaysFromNow;
        } catch (Exception ex) {
            return true;
        }
    }
}