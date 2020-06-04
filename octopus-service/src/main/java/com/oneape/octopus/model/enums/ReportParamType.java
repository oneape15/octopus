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
    MULTI(4, "MULTI");
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
     * 判断value中是否包含某个类型
     *
     * @param value int
     * @param rft   ReportParamType
     * @return boolean true - 包含; false - 不包含
     */
    public static boolean contains(int value, ReportParamType rft) {
        if (value <= 0 || rft == null) return false;

        int code = rft.getCode();

        return code == (code & value);
    }

    /**
     * 拆分成每一项
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
     * 对类型进行合并
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
}
