package com.oneape.octopus.commons.cause;

import com.oneape.octopus.commons.cause.BizException;

/**
 * 未授权异常
 */
public class UnauthorizedException extends BizException {
    /**
     * 默认异常构造器.
     */
    public UnauthorizedException() {
        setCode(StateCode.Unauthorized.getCode());
    }

    public UnauthorizedException(String msg) {
        super(StateCode.Unauthorized.getCode(), msg);
    }
}
