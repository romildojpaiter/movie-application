package com.outsera.movie.domain.exception;


/**
 * Throw when the user does not have the privileges (scopes) to execute an action
 */
public class TimeoutException extends BaseException {

    public TimeoutException(final String message) { super(message); }

    public TimeoutException(final BaseErrorMessage errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

    public TimeoutException() {
        super(BaseErrorMessage.TIMEOUT_ERROR);
    }
}
