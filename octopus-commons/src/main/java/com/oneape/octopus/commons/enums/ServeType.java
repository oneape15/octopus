package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

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
        for (ServeType st : values()) {
            if (StringUtils.equalsIgnoreCase(st.getCode(), code)) {
                return st;
            }
        }
        return null;
    }
}
