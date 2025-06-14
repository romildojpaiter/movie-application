package com.outsera.movie.domain.exception.handler;


import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import com.outsera.movie.controller.response.ErrorField;
import com.outsera.movie.controller.response.ErrorResponse;
import com.outsera.movie.domain.exception.BaseErrorMessage;
import com.outsera.movie.domain.exception.BaseException;
import com.outsera.movie.domain.exception.BusinessException;
import com.outsera.movie.domain.exception.ConflictException;
import com.outsera.movie.domain.exception.InvalidValueException;
import com.outsera.movie.domain.exception.NotFoundException;
import com.outsera.movie.domain.exception.OAuth2Exception;
import com.outsera.movie.domain.exception.RemoteException;
import com.outsera.movie.domain.exception.UnauthorizedException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Hidden
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException ex) {
        log.error("=== ConstraintViolationException ===", ex);
        final List<ErrorField> errorFields = ex.getConstraintViolations()
            .parallelStream()
            .map(fieldError -> new ErrorField(((PathImpl) fieldError.getPropertyPath()).getLeafNode().toString(), fieldError.getMessage()))
            .toList();

        return ErrorResponse.builder()
            .errorCode(InvalidValueException.class.getName())
            .errorDescription(BaseErrorMessage.GENERIC_INVALID_PARAMETER.getMessage())
            .fields(errorFields)
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ErrorResponse handleWebExchangeBindException(final WebExchangeBindException ex) {
        log.error("=== WebExchangeBindException -> {} ===", ex.getLocalizedMessage());
        final List<ErrorField> errorFields = ex.getAllErrors()
            .parallelStream()
            .map(objectError -> {
                var fieldError = objectError.getObjectName();
                if (objectError instanceof FieldError fieldError1) {
                    fieldError = fieldError1.getField();
                }
                return ErrorField.builder()
                    .field(fieldError)
                    .errorDescription(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()))
                    .build();
            })
            .toList();
        return ErrorResponse.builder()
            .errorCode(InvalidValueException.class.getSimpleName())
            .errorDescription(BaseErrorMessage.GENERIC_INVALID_PARAMETER.getMessage())
            .fields(errorFields)
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public ErrorResponse handleServerWebInputException(final ServerWebInputException ex) {
        log.error("=== ServerWebInputException ===", ex);
        List<ErrorField> errorFields = Collections.emptyList();
        if (ex.getMethodParameter() != null) {
            errorFields = List.of(ErrorField.builder()
                .field(ex.getMethodParameter().getParameterName())
                .errorDescription(ex.getReason())
                .build());
        }

        String message = BaseErrorMessage.GENERIC_INVALID_PARAMETER.getMessage();

        if(ex.getRootCause() instanceof IllegalArgumentException) {
            Throwable rootCause = ex.getRootCause();
            message = Objects.nonNull(rootCause) && Objects.nonNull(rootCause.getLocalizedMessage())
                ? rootCause.getLocalizedMessage() : message;

            errorFields = null;
        }

        return ErrorResponse.builder()
            .errorCode(InvalidValueException.class.getSimpleName())
            .errorDescription(message)
            .fields(errorFields)
            .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(final Exception ex) {
        log.error("=== Exception ===", ex);
        return ErrorResponse.builder()
            .errorCode(messageSource.getMessage("generic.default.error.code", null, LocaleContextHolder.getLocale()))
            .errorDescription(messageSource.getMessage("generic.default.error.description", null, LocaleContextHolder.getLocale()))
            .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ErrorResponse handleAccessDenied(final AccessDeniedException ex) {
        log.error("=== AccessDeniedException ===", ex);
        return ErrorResponse.builder()
            .errorCode(ex.getClass().getName())
            .errorDescription(BaseErrorMessage.GENERIC_UNAUTHORIZED_EXCEPTION.getMessage())
            .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({OAuth2Exception.class})
    public ErrorResponse handleAccessDenied(final OAuth2Exception ex) {
        log.error("=== OAuth2Exception ===", ex);
        return ErrorResponse.builder()
            .errorCode(ex.getClass().getName())
            .errorDescription(ex.getMessage() != null ? ex.getMessage() : BaseErrorMessage.GENERIC_UNAUTHORIZED_EXCEPTION.getMessage())
            .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse handleUnauthorized(final UnauthorizedException ex) {
        log.error("=== UnauthorizedException ===", ex);
        return buildError(ErrorResponse.builder(), ex).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BaseException.class)
    public ErrorResponse handleGenericException(final BaseException ex) {
        log.error("=== BaseException ===", ex);
        return buildError(ErrorResponse.builder(), ex).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(final NotFoundException ex) {
        log.error("=== NotFoundException ===", ex);
        return buildError(ErrorResponse.builder(), ex).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidValueException.class)
    public ErrorResponse handleInvalidValueException(final InvalidValueException ex) {
        log.error("=== InvalidValueException ===", ex);
        return buildError(ErrorResponse.builder(), ex).build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorResponse handleConflictException(final ConflictException ex) {
        log.error("=== ConflictException ===", ex);
        return buildError(ErrorResponse.builder(), ex).build();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(RemoteException.class)
    public ErrorResponse handleRemoteException(final RemoteException ex) {
        log.error("=== RemoteException ===", ex);
        return buildError(ErrorResponse.builder(), ex).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ErrorResponse handleBusinessExceptionException(final BusinessException ex) {
        log.error("=== BusinessException ===", ex);
        return buildError(ErrorResponse.builder(), ex).build();
    }

    private ErrorResponse.ErrorResponseBuilder buildError(final ErrorResponse.ErrorResponseBuilder errorBuilder,
                                                          final BaseException ex) {
        return errorBuilder.errorCode(ex.getClass().getSimpleName())
            .errorDescription(ex.getLocalizedMessage());
    }
}
