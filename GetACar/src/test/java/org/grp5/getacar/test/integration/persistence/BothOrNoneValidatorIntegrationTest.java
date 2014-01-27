package org.grp5.getacar.test.integration.persistence;

import com.google.inject.Inject;
import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.persistence.validation.BothOrNone;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import java.util.Set;

@RunWith(OnamiRunner.class)
public class BothOrNoneValidatorIntegrationTest extends BaseValidatorIntegrationTest {

    @Inject
    ValidationHelper validationHelper;

    @BothOrNone.List(
            @BothOrNone(firstField = "first", secondField = "second")
    )
    public class TestClass {
        private Object first;
        private Object second;

        public Object getFirst() {
            return first;
        }

        public void setFirst(Object first) {
            this.first = first;
        }

        public Object getSecond() {
            return second;
        }

        public void setSecond(Object second) {
            this.second = second;
        }
    }

    @Test
    public void invalidValueTest() {
        final TestClass testObject = new TestClass();
        testObject.setFirst(new Object());
        final Set<ConstraintViolation<?>> firstConstraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be invalid", firstConstraintViolations.size() == 2);

        testObject.setFirst(null);
        testObject.setSecond(new Object());
        final Set<ConstraintViolation<?>> secondConstraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be invalid", secondConstraintViolations.size() == 2);
    }

    @Test
    public void validValueTest() {
        final TestClass testObject = new TestClass();
        final Set<ConstraintViolation<?>> firstConstraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be valid", firstConstraintViolations.size() == 0);

        testObject.setFirst(new Object());
        testObject.setSecond(new Object());
        final Set<ConstraintViolation<?>> secondConstraintViolations = validationHelper.validate(testObject);
        Assert.assertTrue("Should be valid", secondConstraintViolations.size() == 0);
    }
}
