package com.outsera.movie.domain.exception;


/**
 * Throw when the user does not have the privileges (scopes) to execute an action
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException(final String message) { super(message); }

    public UnauthorizedException(final BaseErrorMessage errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

    public UnauthorizedException() {
        super(BaseErrorMessage.GENERIC_UNAUTHORIZED_EXCEPTION);
    }
}
