package org.grp5.getacar.persistence.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateTimeBeforeOtherDateTimeValidator.class)
public @interface DateTimeBeforeOtherDateTime {

    String DEFAULT_MESSAGE = "{org.grp5.getacar.persistence.validation.DateTimeBeforeOtherDateTime.message}";

    String message() default DEFAULT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String date();

    /**
     * @return The second field
     */
    String otherDate();

    /**
     * Defines several <code>@DateTimeBeforeOtherDateTime</code> annotations on the same element
     *
     * @see DateTimeBeforeOtherDateTime
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        DateTimeBeforeOtherDateTime[] value();
    }
}
