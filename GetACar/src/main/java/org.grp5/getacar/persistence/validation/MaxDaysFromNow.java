package org.grp5.getacar.persistence.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p/>
 * The annotated element must be a {@link org.joda.time.DateTime} whose value may not be more than two days in the
 * future
 * <p/>
 * <p/>
 * <code>null</code> elements are considered valid
 *
 * @author opn
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MaxDaysFromNowValidator.class})
public @interface MaxDaysFromNow {
    String message() default "{org.grp5.getacar.persistence.validation.MaxDaysFromNow.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return value the element must be higher or equal to
     */
    int value();

    /**
     * Defines several <code>@MaxDaysFromNow</code> annotations on the same element
     *
     * @author opn
     * @see MaxDaysFromNow
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        MaxDaysFromNow[] value();
    }
}