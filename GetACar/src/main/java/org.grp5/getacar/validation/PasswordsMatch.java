package org.grp5.getacar.validation;

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
@Constraint(validatedBy = PasswordsMatchValidator.class)
public @interface PasswordsMatch {
    String DEFAULT_MESSAGE = "{org.grp5.getacar.validation.PasswordsMatch.message}";

    String message() default DEFAULT_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String password();

    /**
     * @return The second field
     */
    String passwordRepeat();

    /**
     * Defines several <code>@PasswordsMatch</code> annotations on the same element
     *
     * @see PasswordsMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        PasswordsMatch[] value();
    }
}
