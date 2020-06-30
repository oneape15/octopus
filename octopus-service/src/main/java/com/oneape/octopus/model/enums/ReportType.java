package com.oneape.octopus.model.enums;

/**
 * Report Type
 */
public enum ReportType {
    /**
     * The data show.
     */
    REPORT(1, "REPORT"),
    /**
     * Interface service type.
     */
    INTERFACE(2, "INTERFACE"),
    /**
     * The list of value.
     */
    LOV(4, "LOV");


    private Integer code;
    private String  desc;

    ReportType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
