package com.oneape.octopus.commons.cause;

import lombok.Getter;

/**
 * Unauthorized exception class.
 */
@Getter
public class UnauthorizedException extends BizException {
    private String msg;

    /**
     * Default constructor.
     */
    public UnauthorizedException() {
        this("");
    }

    public UnauthorizedException(String msg) {
        super(StateCode.Unauthorized.getCode(), msg);
        this.msg = msg;
    }
}
