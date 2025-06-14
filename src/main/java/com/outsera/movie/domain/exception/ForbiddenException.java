package com.outsera.movie.domain.exception;

public class ForbiddenException extends BaseException {

    public ForbiddenException(final BaseErrorMessage errorMessage) {
        super(errorMessage);
    }

    public ForbiddenException(final String message) { super(message); }

    public ForbiddenException() {
        super(BaseErrorMessage.FORBIDDEN_ERROR);
    }
}
