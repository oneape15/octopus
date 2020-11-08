package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 固定的下拉框选择项
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
