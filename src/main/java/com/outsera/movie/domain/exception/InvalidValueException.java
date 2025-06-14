package com.outsera.movie.domain.exception;

public class InvalidValueException extends BaseException {

    public InvalidValueException() {
        super(BaseErrorMessage.GENERIC_INVALID_PARAMETER);
    }

    public InvalidValueException(final BaseErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidValueException(final Throwable aCause) {
        super(BaseErrorMessage.GENERIC_INVALID_PARAMETER, aCause);
    }

    public InvalidValueException(final String additionalMessage, final BaseErrorMessage errorMessage) {
        super(additionalMessage, errorMessage);
    }

    public InvalidValueException(final String message) {
        super(message);
    }
}
