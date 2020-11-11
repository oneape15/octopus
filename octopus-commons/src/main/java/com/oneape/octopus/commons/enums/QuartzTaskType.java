package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-24 16:11.
 * Modify:
 */
public enum QuartzTaskType {
    // The serve task
    SERVE,
    // The custom sql.
    RAW_SQL,
    // Simple email not include appendix.
    SIMPLE_EMAIL;

    public static boolean isValidType(String name) {
        for (QuartzTaskType tt : values()) {
            if (StringUtils.equalsIgnoreCase(name, tt.name())) {
                return true;
            }
        }
        return false;
    }

    public static QuartzTaskType getByName(String name) {
        for (QuartzTaskType tt : values()) {
            if (StringUtils.equalsIgnoreCase(name, tt.name())) {
                return tt;
            }
        }
        return null;
    }
}
