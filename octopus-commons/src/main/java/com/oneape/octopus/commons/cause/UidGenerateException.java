package com.oneape.octopus.commons.cause;

public class UidGenerateException extends RuntimeException {

    /**
     * Default constructor
     */
    public UidGenerateException() {
        super();
    }

    /**
     * Constructor with message & cause
     *
     * @param message String
     * @param cause   Throwable
     */
    public UidGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message
     *
     * @param message String
     */
    public UidGenerateException(String message) {
        super(message);
    }

    /**
     * Constructor with message format
     *
     * @param msgFormat String
     * @param args      Object...
     */
    public UidGenerateException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    /**
     * Constructor with cause
     *
     * @param cause Throwable
     */
    public UidGenerateException(Throwable cause) {
        super(cause);
    }
}
