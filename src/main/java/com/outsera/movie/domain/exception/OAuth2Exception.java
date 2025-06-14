package com.outsera.movie.domain.exception;

public class OAuth2Exception extends BaseException {

    public OAuth2Exception(final BaseErrorMessage errorMessage) {
        super(errorMessage);
    }

    public OAuth2Exception(final String message) { super(message); }

    public OAuth2Exception() {
        super(BaseErrorMessage.FORBIDDEN_ERROR);
    }
}
