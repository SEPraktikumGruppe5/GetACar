package org.grp5.getacar.resource.mapper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.grp5.getacar.persistence.validation.SpecialConstraint;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Multimap<String, String> fieldErrorsMap = ArrayListMultimap.create();

        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            final Set payload = violation.getConstraintDescriptor().getPayload();
            final String propertyPath;
            if (payload.contains(SpecialConstraint.CombinedField.class)) {
                final String deepPropertyPath = violation.getPropertyPath().toString();
                final List<String> pathLevels = Lists.newArrayList(Splitter.on(".").split(deepPropertyPath));
                if (pathLevels.size() > 1) {
                    pathLevels.remove(pathLevels.size() - 1);
                }
                propertyPath = Joiner.on(".").join(pathLevels);
            } else {
                propertyPath = violation.getPropertyPath().toString();
            }
            if (!fieldErrorsMap.get(propertyPath).contains(violation.getMessage())) {
                fieldErrorsMap.put(propertyPath, violation.getMessage());
            }
        }

        @SuppressWarnings("unchecked") // Safe because ArrayListMultimap guarantees this.
        final Map<String, List<String>> toSerialize =
                (Map<String, List<String>>) (Map<?, ?>) fieldErrorsMap.asMap();

        return Response.status(422).entity(Collections.singletonMap("errors",
                toSerialize)).build();
    }
}