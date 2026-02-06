package mrducky.exception;

/**
 * Signals an error in user input or task operations.
 */
public class MrDuckyException extends Exception {
    /**
     * Creates a MrDuckyException with the given message.
     *
     * @param message Exception message.
     */
    public MrDuckyException(String message) {
        super(message);
    }
}
