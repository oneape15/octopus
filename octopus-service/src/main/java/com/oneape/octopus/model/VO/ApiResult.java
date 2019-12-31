package com.oneape.octopus.model.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.Api;
import com.oneape.octopus.common.StateCode;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;

@Data
public class ApiResult<T> implements Serializable {
    /**
     * 返回结果
     */
    private T data;
    /**
     * 错误码
     */
    @JsonProperty("error_code")
    private Integer code;
    /**
     * 错误信息
     */
    @JsonProperty("error_message")
    private String message;

    public ApiResult() {
        this(StateCode.OK);
    }

    public ApiResult(StateCode stateCode) {
        this.code = stateCode.getCode();
        this.message = stateCode.getMessage();
    }

    public ApiResult(StateCode stateCode, T data) {
        this(stateCode);
        this.data = data;
    }

    public ApiResult(T data, Integer code, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResult(T data, Integer code) {
        this.code = code;
        this.message = "";
        this.data = data;
    }

    public ApiResult(T data) {
        this();
        this.data = data;
    }

    public static <T> ApiResult<T> ofData(T data) {
        return new ApiResult<>(data);
    }

    public static <T> ApiResult<T> ofError(StateCode stateCode) {
        return new ApiResult<>(stateCode);
    }

    public static <T> ApiResult<T> ofError(int code, String message) {
        return new ApiResult<>(null, code, message);
    }
}
