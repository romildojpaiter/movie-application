package com.outsera.movie.domain.exception;


/**
 * Throw when the user does not provide a proper authentication (token expired, missing authentication token...)
 */
public class UnauthenticatedException extends BaseException {

    public UnauthenticatedException(final BaseErrorMessage errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

    public UnauthenticatedException() {
        super(BaseErrorMessage.GENERIC_UNAUTHENTICATED_EXCEPTION);
    }

    public UnauthenticatedException(final Throwable throwable) {
        super(BaseErrorMessage.GENERIC_UNAUTHENTICATED_EXCEPTION, throwable);
    }
}
