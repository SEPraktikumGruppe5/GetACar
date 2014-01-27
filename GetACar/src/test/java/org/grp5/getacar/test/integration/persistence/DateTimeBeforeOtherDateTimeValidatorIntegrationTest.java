package org.grp5.getacar.test.integration.persistence;

import com.google.inject.Inject;
import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.persistence.validation.DateTimeBeforeOtherDateTime;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

@RunWith(OnamiRunner.class)
public class DateTimeBeforeOtherDateTimeValidatorIntegrationTest extends BaseValidatorIntegrationTest {

    @Inject
    ValidationHelper validationHelper;

    @DateTimeBeforeOtherDateTime.List(
            value = {@DateTimeBeforeOtherDateTime(date = "startTime", otherDate = "endTime")}
    )
    public class TestClass {
        private DateTime startTime;
        private DateTime endTime;

        public DateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(DateTime startTime) {
            this.startTime = startTime;
        }

        public DateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(DateTime endTime) {
            this.endTime = endTime;
        }
    }

    @Test
    public void invalidValueTest() {
        final TestClass testObject = new TestClass();
        testObject.setStartTime(new DateTime(2013, 12, 12, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        testObject.setEndTime(new DateTime(2013, 12, 12, 9, 59, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be invalid", constraintViolations.size() == 2);
    }

    @Test
    public void validValueTest() {
        final TestClass testObject = new TestClass();
        testObject.setStartTime(new DateTime(2013, 12, 12, 10, 0, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        testObject.setEndTime(new DateTime(2013, 12, 12, 10, 1, DateTimeZone.forTimeZone(europeBerlinTimeZone)));
        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be valid", constraintViolations.size() == 0);
    }
}
