package com.outsera.movie.domain.exception;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/** Top class of all the error messages. Any error messages must extend this */
@RequiredArgsConstructor
public class BaseErrorMessage {

    private static final String DEFAULT_RESOURCE = "messages.errors.ErrorMessages";

    public static final BaseErrorMessage GENERIC_EXCEPTION = new BaseErrorMessage("generic");
    public static final BaseErrorMessage GENERIC_UNAUTHENTICATED_EXCEPTION = new BaseErrorMessage("generic.unauthenticated");
    public static final BaseErrorMessage GENERIC_NOT_FOUND_EXCEPTION = new BaseErrorMessage("generic.notFound");
    public static final BaseErrorMessage GENERIC_UNAUTHORIZED_EXCEPTION = new BaseErrorMessage("generic.unauthorized");
    public static final BaseErrorMessage GENERIC_METHOD_NOT_ALLOWED = new BaseErrorMessage("generic.methodNotAllowed");
    public static final BaseErrorMessage GENERIC_INVALID_PARAMETER = new BaseErrorMessage("generic.invalidParameter");
    public static final BaseErrorMessage BAD_REQUEST_ERROR = new BaseErrorMessage("error.bad_request");
    public static final BaseErrorMessage FORBIDDEN_ERROR = new BaseErrorMessage("error.forbidden");
    public static final BaseErrorMessage TIMEOUT_ERROR = new BaseErrorMessage("error.timeout");
    public static final BaseErrorMessage GENERIC_INVALID_VALUE = new BaseErrorMessage("generic.invalid.value");
    public static final BaseErrorMessage GENERIC_ORDER_NOT_FOUND = new BaseErrorMessage("generic.notFound.order");
    public static final BaseErrorMessage GENERIC_CONFLICT_ORDER = new BaseErrorMessage("generic.conflict.order");
    public static final BaseErrorMessage GENERIC_MAX_RETRIES = new BaseErrorMessage("generic.maxRetries");
    public static final BaseErrorMessage INVALID_CUSTOMER_ID = new BaseErrorMessage("authToken.invalid.customerId");
    public static final BaseErrorMessage GENERIC_FORBIDDEN_EXCEPTION = new BaseErrorMessage("generic.forbidden");

    private final String key;
    private String[] params;

    public BaseErrorMessage withParams(final String... params) {
        this.params = ArrayUtils.clone(params);
        return this;
    }

    public String getMessage() {
        String message = tryGetMessageFromBundle();
        if (params != null) {
            final MessageFormat fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }

    protected String tryGetMessageFromBundle() {
        final String result;
        if (getResource().containsKey(key)) {
            result = getResource().getString(key);
        } else {
            result = ResourceBundle.getBundle(DEFAULT_RESOURCE).getString(key);
        }
        return result;
    }

    public ResourceBundle getResource() {
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}