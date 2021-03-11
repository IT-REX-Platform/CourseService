package de.uni_stuttgart.it_rex.course.service.written.exception;

public class ChapterNotFoundException extends RuntimeException {
    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public ChapterNotFoundException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     * @param cause   The cause for the exception.
     */
    public ChapterNotFoundException(final String message,
                                    final Throwable cause) {
        super(message, cause);
    }
}
