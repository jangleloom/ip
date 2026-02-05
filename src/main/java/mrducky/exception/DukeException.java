package mrducky.exception;

/**
 * Signals an error in user input or task operations.
 */
public class DukeException extends Exception {
    /**
     * Creates a DukeException with the given message.
     *
     * @param message Exception message.
     */
    public DukeException(String message) {
        super(message);
    }
}
