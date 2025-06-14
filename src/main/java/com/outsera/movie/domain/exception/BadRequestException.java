package com.outsera.movie.domain.exception;

public class BadRequestException extends BaseException {

    public BadRequestException(final BaseErrorMessage errorMessage) {
        super(errorMessage);
    }

    public BadRequestException(final String message) { super(message); }

    public BadRequestException() {
        super(BaseErrorMessage.BAD_REQUEST_ERROR);
    }
}
