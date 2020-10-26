package com.oneape.octopus.model.enums;

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
}
