package ru.edjll.backend.handler;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.edjll.backend.exception.ResponseParameterException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public GlobalResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errors", ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", ex.getConstraintViolations()
                .stream()
                .collect(
                        Collectors.toMap(cv -> cv.getPropertyPath().toString().replaceAll(".+\\.", ""),
                        ConstraintViolation::getMessage)
                )
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Object> handleRestClientResponseException(RestClientResponseException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        JSONParser jsonParser = new JSONParser(ex.getResponseBodyAsString());

        body.put("timestamp", new Date());
        body.put("status", ex.getRawStatusCode());
        try {
            body.put("errors", jsonParser.object());
        } catch (ParseException ignored) { }

        return new ResponseEntity<>(body, HttpStatus.valueOf(ex.getRawStatusCode()));
    }

    @ExceptionHandler(ResponseParameterException.class)
    public ResponseEntity<Object> handleStatusException(ResponseParameterException exception, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", new Date());
        body.put("status", exception.getStatus().value());
        if (exception.getReason() != null) {
            body.put(
                    "errors",
                    Collections.singletonMap(
                            exception.getField(),
                            String.format(messageSource.getMessage(exception.getReason(), null, Locale.getDefault()), exception.getValue())
                    )
            );
        } else {
            body.put("errors", "");
        }

        return new ResponseEntity<>(body, exception.getStatus());
    }
}
