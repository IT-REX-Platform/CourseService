package de.uni_stuttgart.it_rex.course.service.written.exception;

public class InvalidTimeRangeException extends RuntimeException {
    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public InvalidTimeRangeException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     * @param cause   The cause for the exception.
     */
    public InvalidTimeRangeException(final String message,
                                     final Throwable cause) {
        super(message, cause);
    }
}
