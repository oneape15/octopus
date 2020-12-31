package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 16:57.
 * Modify:
 */
public enum ServeStatusType {
    EDIT,
    PUBLISH,
    OFFLINE,
    ARCHIVE;

    public static ServeStatusType getByCode(String code) {
        if (StringUtils.isBlank(code)) return null;

        for (ServeStatusType sst : values()) {
            if (StringUtils.equalsIgnoreCase(code, sst.name())) {
                return sst;
            }
        }
        return null;
    }
}
