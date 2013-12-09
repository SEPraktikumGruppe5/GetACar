package org.grp5.getacar.persistence.validation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * Provides helper methods related to validation.
 */
public interface ValidationHelper {

    /**
     * Validates an object.
     *
     * @param object The object to validate
     * @return The set containing the constraint violations
     */
    <E> Set<ConstraintViolation<?>> validate(E object);

    /**
     * Validates an object.
     *
     * @param object The object to validate
     * @throws ConstraintViolationException Thrown if at least one of the constraints is violated
     */
    <E> void validateAndThrow(E object) throws ConstraintViolationException;
}