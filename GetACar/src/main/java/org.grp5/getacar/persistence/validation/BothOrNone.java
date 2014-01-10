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
@Constraint(validatedBy = BothOrNoneValidator.class)
public @interface BothOrNone {
    String DEFAULT_MESSAGE = "{org.grp5.getacar.persistence.validation.BothOrNone.message}";

    String message() default DEFAULT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String firstField();

    /**
     * @return The second field
     */
    String secondField();

    /**
     * Defines several <code>@BothOrNone</code> annotations on the same element
     *
     * @see org.grp5.getacar.persistence.validation.BothOrNone
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        BothOrNone[] value();
    }
}
