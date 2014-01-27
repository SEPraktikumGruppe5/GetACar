package org.grp5.getacar.test.integration.persistence;

import com.google.inject.Inject;
import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.persistence.validation.FutureDateTime;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

@RunWith(OnamiRunner.class)
public class FutureDateTimeValidatorIntegrationTest extends BaseValidatorIntegrationTest {

    @Inject
    ValidationHelper validationHelper;

    public class TestClass {
        private DateTime time;

        @FutureDateTime
        public DateTime getTime() {
            return time;
        }

        public void setTime(DateTime startTime) {
            this.time = startTime;
        }
    }

    @Test
    public void invalidValueTest() {
        final TestClass testObject = new TestClass();
        final DateTime now = new DateTime();
        testObject.setTime(now);
        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be invalid", constraintViolations.size() == 1);
    }

    @Test
    public void validValueTest() {
        final TestClass testObject = new TestClass();
        final DateTime now = new DateTime();
        testObject.setTime(now.plusMinutes(1));
        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be valid", constraintViolations.size() == 0);
    }
}
