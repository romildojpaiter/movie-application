package com.outsera.movie.domain.exception;


/** In case some resource is not found */
public class NotFoundException extends BaseException {

    public NotFoundException() {
        super(BaseErrorMessage.GENERIC_NOT_FOUND_EXCEPTION);
    }

    public NotFoundException(final String message) { super(message); }

    public NotFoundException(final BaseErrorMessage errorMessage) {
        super(errorMessage);
    }

    public NotFoundException(final String additionalMessage, final BaseErrorMessage errorMessage) {
        super(additionalMessage, errorMessage);
    }
}
