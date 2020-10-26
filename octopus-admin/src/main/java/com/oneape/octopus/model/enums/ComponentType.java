package com.oneape.octopus.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Front-end component type.
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 17:39.
 * Modify:
 */
public enum ComponentType {
    INPUT,
    INPUT_RANGE,
    SWITCH,
    RADIO,
    CHECK_BOX,
    SELECTOR,
    DATETIME,
    DATE_RANGE;

    public static ComponentType getByName(String ctName) {
        if (StringUtils.isBlank(ctName)) return null;

        for (ComponentType ct : values()) {
            if (StringUtils.equalsIgnoreCase(ct.name(), ctName)) {
                return ct;
            }
        }
        return null;
    }
}
