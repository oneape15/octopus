package com.oneape.octopus.model.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * The report param type.
 */
public enum ReportParamType {
    NORMAL(0, "NORMAL"),
    INNER(1, "INNER"),
    BETWEEN(2, "BETWEEN"),
    MULTI(4, "MULTI"),
    LOV(8, "LOV"),
    STATIC_LOV(16, "STATIC_LOV");

    private int    code;
    private String desc;

    ReportParamType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isSupport(int value) {
        return code == (code & value);
    }

    /**
     * Determines whether the value contains the type。
     *
     * @param value int
     * @param rft   ReportParamType
     * @return boolean true - contains; false - not contains
     */
    public static boolean contains(int value, ReportParamType rft) {
        if (value <= 0 || rft == null) return false;

        int code = rft.getCode();

        return code == (code & value);
    }

    /**
     * Break it down into each of these terms.
     *
     * @param value int
     * @return List
     */
    public static List<Integer> split(int value) {
        List<Integer> arr = new ArrayList<>();
        if (value > 0) {
            for (ReportParamType rft : values()) {
                int code = rft.getCode();
                if ((code & value) == code) {
                    arr.add(code);
                }
            }
        }

        return arr;
    }

    /**
     * Merge the types
     *
     * @param types List
     * @return int
     */
    public static int merge(List<Integer> types) {
        if (types == null || types.size() == 0) return 0;

        int ret = 0;
        for (Integer tmp : types) {
            ret |= tmp;
        }
        return ret;
    }

    /**
     * check the type contain lov.
     *
     * @param type param type
     */
    public static boolean isLov(Integer type) {
        return (contains(type, LOV) || contains(type, STATIC_LOV));
    }

    public static boolean isStaticLov(Integer type) {
        return contains(type, STATIC_LOV);
    }

    /**
     * Determine if the parameter type is a multi-valued parameter
     *
     * @param type param type
     */
    public static boolean isMultiValue(Integer type) {
        return (contains(type, BETWEEN) || contains(type, MULTI));
    }

    /**
     * Determine if the parameter type is a range-valued parameter
     *
     * @param type param type
     */
    public static boolean isRangeValue(Integer type) {
        return contains(type, BETWEEN);
    }
}
