package org.grp5.getacar.persistence.validation;

import javax.validation.Payload;

public class SpecialConstraint {
    /**
     *  Indicates that the validated field is 'combined', what means that the last part of the propertyPath should be
     *  cut off before sending it back to the client.
     */
    public static class CombinedField implements Payload {
    }
}