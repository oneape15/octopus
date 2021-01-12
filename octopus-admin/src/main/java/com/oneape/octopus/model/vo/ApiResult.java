package com.oneape.octopus.model.vo;

import com.oneape.octopus.commons.cause.StateCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResult<T> implements Serializable {
    /**
     * 返回结果
     */
    private T       data;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String  msg;

    public ApiResult() {
        this(StateCode.OK);
    }

    public ApiResult(StateCode stateCode) {
        this.code = stateCode.getCode();
        this.msg = stateCode.getMessage();
    }

    public ApiResult(StateCode stateCode, T data) {
        this(stateCode);
        this.data = data;
    }

    public ApiResult(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public ApiResult(T data, Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(T data, Integer code) {
        this.code = code;
        this.msg = "";
        this.data = data;
    }

    public ApiResult(T data) {
        this();
        this.data = data;
    }

    public boolean isSuccess() {
        return code != null && code == StateCode.OK.getCode();
    }

    public static <T> ApiResult<T> ofData(T data) {
        return new ApiResult<>(data);
    }

    public static <T> ApiResult<T> ofMessage(String msg) {
        return new ApiResult<>(StateCode.OK.getCode(), msg);
    }

    public static <T> ApiResult<T> ofError(StateCode stateCode) {
        return new ApiResult<>(stateCode);
    }

    public static <T> ApiResult<T> ofError(int code, String msg) {
        return new ApiResult<>(null, code, msg);
    }

    public static <T> ApiResult<T> ofError(StateCode stateCode, String msg) {
        ApiResult<T> ret = new ApiResult<>(stateCode);
        ret.setMsg(msg);
        return ret;
    }
}
