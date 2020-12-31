package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Serve Type
 */
public enum ServeType {
    /**
     * The visual type.
     */
    REPORT("REPORT", "The visual type"),
    /**
     * Interface service type.
     */
    INTERFACE("INTERFACE", "The interface type");


    private String code;
    private String desc;

    private static Map<String, ServeType> map;

    static {
        map = new HashMap<>();
        for (ServeType st : values()) {
            map.put(st.getCode(), st);
        }
    }

    ServeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ServeType getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        return map.getOrDefault(StringUtils.upperCase(code), null);
    }

    public static boolean isValid(String code) {
        if (StringUtils.isBlank(code)) return false;

        return map.getOrDefault(StringUtils.upperCase(code), null) != null;
    }
}
