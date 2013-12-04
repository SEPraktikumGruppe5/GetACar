package org.grp5.getacar.mapper;

/**
 * Helper class used to generate the JSON output of {@link ValidationExceptionMapper}.
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ValidationMessage {
    private final String path;
    private final String message;

    public ValidationMessage(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }
}
