package com.martzatech.vdhg.crmprojectback.infrastructure.configs;

import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.ErrorResponseToken;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.UnauthorizedException;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ErrorResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ErrorResponse.ErrorFieldResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomRestControllerAdvice {

    private static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource not found";

    private static final String SOME_FIELD_IS_MISSING_CHECK_IT_MESSAGE = "Some validations have failed, check it.";

    private static final String CANNOT_FOUND_PROPERTY_MESSAGE = "Cannot found property: ";

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseToken> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorResponseToken errorResponse = new ErrorResponseToken(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleNotFoundException(final NotFoundException ex, final WebRequest request) {
        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(RESOURCE_NOT_FOUND_MESSAGE)
                .id(ex.getId())
                .resource(ex.getResource())
                .build();
    }

    @ExceptionHandler(value = FieldIsMissingException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse handleFieldIsMissingException(final FieldIsMissingException ex, final WebRequest request) {
        return ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(SOME_FIELD_IS_MISSING_CHECK_IT_MESSAGE)
                .errorFields(
                        Collections.singletonList(
                                ErrorFieldResponse.builder().field(ex.getName()).message(ex.getMessage()).build()
                        )
                )
                .build();
    }

    @ExceptionHandler(value = FieldIsDuplicatedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse handleFieldIsDuplicatedException(final FieldIsDuplicatedException ex,
                                                             final WebRequest request) {
        return ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(SOME_FIELD_IS_MISSING_CHECK_IT_MESSAGE)
                .errorFields(
                        Collections.singletonList(
                                ErrorFieldResponse.builder().field(ex.getName()).message(ex.getMessage()).build()
                        )
                )
                .build();
    }

    @ExceptionHandler(value = BusinessRuleException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse handleBusinessRuleException(final BusinessRuleException ex, final WebRequest request) {
        return ErrorResponse.builder().code(HttpStatus.CONFLICT.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(value = PropertyReferenceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse handlePropertyReferenceException(final PropertyReferenceException ex,
                                                             final WebRequest request) {
        return ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(CANNOT_FOUND_PROPERTY_MESSAGE + ex.getPropertyName())
                .build();
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException ex,
                                                                  final WebRequest request) {
        if (Objects.nonNull(ex.getCause())
                && ex.getCause() instanceof ConstraintViolationException
                && Objects.nonNull(ex.getCause().getCause())
                && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
            final SQLIntegrityConstraintViolationException sqlEx = (SQLIntegrityConstraintViolationException) ex.getCause()
                    .getCause();
            return ErrorResponse.builder()
                    .code(sqlEx.getErrorCode())
                    .message(sqlEx.getMessage())
                    .build();
        }
        return ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                                  final WebRequest request) {
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(SOME_FIELD_IS_MISSING_CHECK_IT_MESSAGE)
                .errorFields(
                        ex.getBindingResult().getFieldErrors()
                                .stream()
                                .map(fieldError ->
                                        ErrorFieldResponse.builder()
                                                .field(fieldError.getField())
                                                .message(fieldError.getDefaultMessage())
                                                .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

}
