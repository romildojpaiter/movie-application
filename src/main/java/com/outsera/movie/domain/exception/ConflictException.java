package com.outsera.movie.domain.exception;

public class ConflictException extends BaseException {

    public ConflictException(final BaseErrorMessage errorMessage) {
        super(errorMessage);
    }

    public ConflictException() {
        super(BaseErrorMessage.GENERIC_EXCEPTION);
    }
}
