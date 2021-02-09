package com.oneape.octopus.parse.data;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-09 09:42.
 * Modify:
 */
public class SyntaxException extends RuntimeException {

    public SyntaxException(final String message) {
        super(message);
    }

    public SyntaxException(final Throwable cause) {
        super(cause);
    }

    public SyntaxException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
