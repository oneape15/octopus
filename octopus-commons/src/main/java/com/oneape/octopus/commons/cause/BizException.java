package com.oneape.octopus.commons.cause;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {
    // Abnormal service coding.
    private int    code;
    // Abnormal service message.
    private String msg;

    /**
     * Constructs a new biz exception with {@code null} as its detail message and code.
     * The cause is not initialized.
     */
    public BizException() {
        this(StateCode.BizError.getCode());
    }

    /**
     * Constructs a new biz exception with the specified detail code.
     * The cause is not initialized.
     *
     * @param code int    Abnormal service coding.
     */
    public BizException(final int code) {
        this(code, "");
    }

    /**
     * Constructs a new biz exception with the specified detail message.
     * The cause is not initialized.
     *
     * @param message String Abnormal service message.
     */
    public BizException(final String message) {
        this(StateCode.BizError.getCode(), message);
    }

    /**
     * Constructs a new biz exception with the specified cause.
     * The message and code is default value.
     *
     * @param cause Throwable
     */
    public BizException(final Throwable cause) {
        this(StateCode.BizError.getCode(), StateCode.BizError.getMessage(), cause);
    }

    /**
     * Constructs a new biz exception with the specified detail message and code.
     * The cause is not initialized.
     *
     * @param code    int    Abnormal service coding.
     * @param message String Abnormal service message.
     */
    public BizException(final int code, final String message) {
        this(code, message, null);
    }

    /**
     * Constructs a new biz exception with the specified detail message, code and cause.
     *
     * @param code    int    Abnormal service coding.
     * @param message String Abnormal service message.
     * @param cause   Throwable
     */
    public BizException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.msg = message;
    }

    /**
     * Constructs a new biz exception with the specified detail message and cause.
     * THe code is default value.
     *
     * @param message String Abnormal service message.
     */
    public BizException(final String message, final Throwable cause) {
        this(StateCode.BizError.getCode(), message, cause);
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return this.msg;
    }
}
