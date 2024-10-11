package edu.rpi.legup.app;

/**
 * {@code InvalidConfigException} is a custom exception class for handling invalid configuration
 * errors
 */
public class InvalidConfigException extends Exception {
    /**
     * Constructs a new InvalidConfigException with the specified detail message
     *
     * @param message the detail message
     */
    public InvalidConfigException(String message) {
        super(message);
    }
}
