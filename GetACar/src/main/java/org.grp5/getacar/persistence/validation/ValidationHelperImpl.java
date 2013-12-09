package org.grp5.getacar.persistence.validation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class ValidationHelperImpl implements ValidationHelper {

    private final Validator validator;

    @Inject
    public ValidationHelperImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <E extends Object> Set<ConstraintViolation<?>> validate(E entity) {
        return Sets.<ConstraintViolation<?>>newHashSet(validator.validate(entity));
    }

    @Override
    public <E extends Object> void validateAndThrow(E entity) throws ConstraintViolationException {
        final Set<ConstraintViolation<?>> constraintViolations = validate(Preconditions.checkNotNull(entity));
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(Sets.<ConstraintViolation<?>>newHashSet(constraintViolations));
        }
    }
}
