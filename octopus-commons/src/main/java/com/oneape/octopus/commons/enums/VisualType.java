package com.oneape.octopus.commons.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * The report visual Type
 */
public enum VisualType {
    /**
     * tabulation
     */
    TABLE(1, "TABLE"),
    /**
     * linear graph
     */
    LINE(2, "LINE"),
    /**
     * bar graph
     */
    BAR(4, "BAR"),
    /**
     * pie chart
     */
    PIE(8, "PIE"),
    /**
     * radar map
     */
    RADAR(16, "RADAR");


    private Integer code;
    private String  desc;

    private static Map<Integer, VisualType> map;

    static {
        map = new HashMap<>();
        for (VisualType vt : values()) {
            map.put(vt.getCode(), vt);
        }
    }

    VisualType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean isValid(Integer code) {
        if (code == null || code < 0) return false;

        return map.getOrDefault(code, null) != null;
    }
}
