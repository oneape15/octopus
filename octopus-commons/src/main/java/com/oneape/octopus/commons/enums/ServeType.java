package com.oneape.octopus.commons.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
    INTERFACE("INTERFACE", "The interface type"),
    /**
     * Aggregate interface.
     */
    AGG("AGG", "Aggregate interface"),
    /**
     * List of Value
     */
    LOV("LOV", "List Of Value"),
    /**
     * static text
     */
    TEXT("TEXT", "static text");


    private final String code;
    private final String desc;

    private static final LinkedHashMap<String, ServeType> map;

    static {
        map = new LinkedHashMap<>();
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

    public static List<ServeType> getList() {
        return new ArrayList<>(map.values());
    }
}
