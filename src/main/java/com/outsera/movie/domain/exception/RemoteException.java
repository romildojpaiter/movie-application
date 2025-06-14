package com.outsera.movie.domain.exception;


/** Problems that happened when doing remote calls (WebClient) */
public class RemoteException extends BaseException {

    public RemoteException() {
        super(BaseErrorMessage.GENERIC_EXCEPTION);
    }

    public RemoteException(final String message) { super(message); }

    public RemoteException(final BaseErrorMessage errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

    public RemoteException(final String additionalMessage, final BaseErrorMessage errorMessage) {
        super(additionalMessage, errorMessage);
    }
}
