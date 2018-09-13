package org.brownmun.web.support;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Handles invalid method argument exceptions from Spring MVC controllers.
 * Usually, this means the body of an API call was invalid, so we return the
 * validation errors as JSON.
 */
@RestControllerAdvice
public class InvalidRequestExceptionHandler extends ResponseEntityExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(InvalidRequestExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public InvalidRequestExceptionHandler(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request)
    {
        log.warn("Handling invalid method argument", ex);

        Map<String, List<String>> errors = Maps.newHashMap();

        // Stick global errors under _global
        List<String> globalErrors = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(error -> messageSource.getMessage(error, Locale.US))
                .collect(Collectors.toList());
        errors.put("_global", globalErrors);

        for (FieldError error : ex.getBindingResult().getFieldErrors())
        {
            String message = messageSource.getMessage(error, Locale.US);
            errors.computeIfAbsent(error.getField(), k -> Lists.newArrayList()).add(message);
        }

        return new ResponseEntity<>(errors, headers, status);
    }
}
