package org.grp5.getacar.mapper;

import org.codehaus.jackson.annotate.JsonUnwrapped;
import org.codehaus.jackson.annotate.JsonValue;

/**
 *
 */
public class StatusResponse {
    private final int status;

    private final Object response;

    public StatusResponse(int status, Object response) {
        this.status = status;
        this.response = response;
    }

    @JsonValue
    public int getStatus() {
        return status;
    }

    @JsonValue
    @JsonUnwrapped
    public Object getResponse() {
        return response;
    }
}
