package com.oneape.octopus.model.enums;

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
}
