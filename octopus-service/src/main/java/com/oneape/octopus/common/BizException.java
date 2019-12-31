package com.oneape.octopus.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {
    private int code;    //异常业务编码

    /**
     * 默认异常构造器.
     */
    public BizException() {
        this(StateCode.InternalServerError);
    }

    public BizException(StateCode stateCode) {
        super(stateCode.getMessage());
        this.code = stateCode.getCode();
    }

    /**
     * 根据错误码和错误信息构造异常.
     *
     * @param code    错误码
     * @param message 异常信息.
     */
    public BizException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    /**
     * 根据异常信息和原生异常构造对象.
     *
     * @param code    错误码
     * @param message 异常信息.
     * @param cause   原生异常.
     */
    public BizException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 根据异常信息和原生异常构造对象.
     *
     * @param message 异常信息.
     * @param cause   原生异常.
     */
    public BizException(final String message, final Throwable cause) {
        this(StateCode.InternalServerError.getCode(), message, cause);
    }

    /**
     * 根据异常信息构造对象.
     *
     * @param message 异常信息.
     */
    public BizException(final String message) {
        this(StateCode.InternalServerError.getCode(), message);
    }


    /**
     * 根据原生异常构造对象.
     *
     * @param cause 原生异常.
     */
    public BizException(final Throwable cause) {
        this(StateCode.InternalServerError, cause);
    }

    public BizException(final StateCode stateCode, final Throwable cause) {
        this(stateCode.getCode(), stateCode.getMessage(), cause);
    }

    @Override
    public String toString() {
        return "BizException{" +
                "code='" + code + '\'' +
                ",message='" + getMessage() + '\'' +
                "} ";
    }
}
