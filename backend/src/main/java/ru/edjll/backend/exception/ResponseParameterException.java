package ru.edjll.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResponseParameterException extends ResponseStatusException {

    private final String field;
    private final String value;

    public ResponseParameterException(HttpStatus status, String field, String value) {
        this(status, field, value, null, null);
    }

    public ResponseParameterException(HttpStatus status, String field, String value, String reason) {
        this(status, field, value, reason,  null);
    }

    public ResponseParameterException(HttpStatus status, String field, String value, String reason, Throwable cause) {
        super(status, reason, cause);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
