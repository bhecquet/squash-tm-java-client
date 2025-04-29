package io.github.bhecquet.exceptions;

/**
 * Exception thrown when binding CUF fails
 */
public class BindCufException extends SquashTmException {
    public BindCufException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BindCufException(String message) {
        super(message);
    }
}
