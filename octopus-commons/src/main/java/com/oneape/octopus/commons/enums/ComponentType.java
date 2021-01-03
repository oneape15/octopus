package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;

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

    private static LinkedHashMap<String, ComponentType> map;

    static {
        for (ComponentType ct : values()) {
            map.put(ct.name(), ct);
        }
    }

    public static ComponentType getByName(String ctName) {
        if (StringUtils.isBlank(ctName)) return null;

        for (ComponentType ct : map.values()) {
            if (StringUtils.equalsIgnoreCase(ct.name(), ctName)) {
                return ct;
            }
        }
        return null;
    }
}
