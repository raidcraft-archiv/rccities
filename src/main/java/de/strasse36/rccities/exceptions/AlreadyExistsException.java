package de.strasse36.rccities.exceptions;

/**
 * Author: Philip Urban
 * Date: 15.02.12 - 20:10
 * Description:
 */
public class AlreadyExistsException extends Throwable
{
    /**
     * Constructs a new throwable with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * <p/>
     * <p>The {@link #fillInStackTrace()} method is called to initialize
     * the stack trace data in the newly created throwable.
     */
    public AlreadyExistsException() {
    }

    /**
     * Constructs a new throwable with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     * <p/>
     * <p>The {@link #fillInStackTrace()} method is called to initialize
     * the stack trace data in the newly created throwable.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AlreadyExistsException(String message) {
        super(message);
    }
}
