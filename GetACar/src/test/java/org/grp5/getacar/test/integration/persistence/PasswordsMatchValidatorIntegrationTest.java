package org.grp5.getacar.test.integration.persistence;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.apache.onami.test.OnamiRunner;
import org.grp5.getacar.persistence.entity.User;
import org.grp5.getacar.persistence.validation.PasswordsMatch;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

@RunWith(OnamiRunner.class)
public class PasswordsMatchValidatorIntegrationTest extends BaseValidatorIntegrationTest {

    @Inject
    ValidationHelper validationHelper;

    @Test
    public void invalidValueTest() {
        final User user = new User();
        user.setPassword("admin");
        user.setPasswordRepeat("nimda");

        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(user);

        Map<String, Class<? extends Annotation>> violatedPathsAndAnnotationClasses = Maps.newHashMap();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            violatedPathsAndAnnotationClasses.put(constraintViolation.getPropertyPath().toString(),
                    (constraintViolation).getConstraintDescriptor().getAnnotation().annotationType());
        }
        Assert.assertTrue("Should be invalid", (violatedPathsAndAnnotationClasses.containsKey("password") &&
                violatedPathsAndAnnotationClasses.get("password").equals(PasswordsMatch.class)));
    }

    @Test
    public void validValueTest() {
        final User user = new User();
        user.setPassword("admin");
        user.setPasswordRepeat("admin");

        final Set<ConstraintViolation<?>> constraintViolations = validationHelper.validate(user);

        Map<String, Class<? extends Annotation>> violatedPathsAndAnnotationClasses = Maps.newHashMap();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            violatedPathsAndAnnotationClasses.put(constraintViolation.getPropertyPath().toString(),
                    (constraintViolation).getConstraintDescriptor().getAnnotation().annotationType());
        }
        Assert.assertTrue("Should be invalid", (!violatedPathsAndAnnotationClasses.containsValue(PasswordsMatch.class)));
    }
}
