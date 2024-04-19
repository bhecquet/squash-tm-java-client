package fr.bhecquet.exceptions;

public class SquashTmException extends RuntimeException {

    public SquashTmException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public SquashTmException(final String message) {
        super(message);
    }
}
