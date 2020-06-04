package com.oneape.octopus.model.enums;

/**
 * Report Type
 */
public enum ReportType {
    /**
     * 表格
     */
    TABLE("TABLE", "表格"),
    /**
     * 线性图
     */
    LINE("LIN", "线性图"),
    /**
     * 柱状图
     */
    BAR("BAR", "柱状图"),
    /**
     * 饼状图
     */
    PIE("PIE", "饼图"),
    /**
     * 雷达图
     */
    RADAR("RADAR", "雷达图");


    private String code;
    private String desc;

    ReportType(String code, String desc) {
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
