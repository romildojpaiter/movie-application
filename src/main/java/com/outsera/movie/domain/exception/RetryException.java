package com.outsera.movie.domain.exception;

public class RetryException extends BaseException {

    public RetryException(final BaseErrorMessage errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }
}
