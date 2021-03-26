package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Fixed drop-down box selection.
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-08 18:07.
 * Modify:
 */
public enum FixOptionType {
    NULL,
    ALL,
    DEFAULT;


    public static FixOptionType byName(String name) {
        if (StringUtils.isBlank(name)) {
            return DEFAULT;
        }

        for (FixOptionType fot : values()) {
            if (StringUtils.equalsIgnoreCase(name, fot.name())) {
                return fot;
            }
        }
        return DEFAULT;
    }

}
