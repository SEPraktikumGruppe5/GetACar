package org.grp5.getacar.mapper;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Multimap<String, String> fieldErrorsMap = ArrayListMultimap.create();

        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            fieldErrorsMap.put(violation.getPropertyPath().toString(),
                    violation.getMessage());
        }

        @SuppressWarnings("unchecked") // Safe because ArrayListMultimap guarantees this.
        final Map<String, List<String>> toSerialize =
                (Map<String, List<String>>) (Map<?, ?>) fieldErrorsMap.asMap();

        return Response.status(422).entity(Collections.singletonMap("errors",
                toSerialize)).build();
    }
}