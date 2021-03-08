package com.oneape.octopus.dto.system;

/**
 * The application type.
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-11 19:43.
 * Modify:
 */
public enum AppType {
    MANAGE(0),
    PC(1),
    MOBILE(2);

    private final Integer code;

    AppType(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
