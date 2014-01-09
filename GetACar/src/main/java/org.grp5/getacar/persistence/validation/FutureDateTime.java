package org.grp5.getacar.persistence.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must be a date in the future.
 * Now is defined as the current time according to the virtual machine
 * <p/>
 * Supported types are:
 * <ul>
 * <li><code>org.joda.time.DateTime</code></li>
 * </ul>
 * <p/>
 * <code>null</code> elements are considered valid.
 *
 * @author opn
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FutureDateTimeValidator.class)
public @interface FutureDateTime {
    String message() default "{javax.validation.constraints.Future.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several <code>@FutureDateTime</code> annotations on the same element
     *
     * @author opn
     * @see FutureDateTime
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FutureDateTime[] value();
    }
}