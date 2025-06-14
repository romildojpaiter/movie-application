package com.outsera.movie.domain.exception;

import lombok.Getter;

/**
 * Generic exception for all the issues in the server. Before sending this
 * exception, always check if there is no specific exception
 */
public class BaseException extends Exception {

    @Getter
    private final String additionalMessage;

    public BaseException() {
        super(BaseErrorMessage.GENERIC_EXCEPTION.getMessage());
        additionalMessage = null;
    }

    public BaseException(String message) {
        super(message);
        additionalMessage = null;
    }

    public BaseException(final BaseErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        additionalMessage = null;
    }

    public BaseException(final BaseErrorMessage errorMessage, final Throwable cause) {
        super(errorMessage.getMessage(), cause);
        if (cause instanceof BaseException baseException) {
            additionalMessage = baseException.additionalMessage;
        } else {
            additionalMessage = null;
        }
    }

    public BaseException(final String additionalMessage, final BaseErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.additionalMessage = additionalMessage;
    }

}