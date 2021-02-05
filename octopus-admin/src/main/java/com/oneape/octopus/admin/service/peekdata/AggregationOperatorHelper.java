package com.oneape.octopus.admin.service.peekdata;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 聚合算子
 */
public enum AggregationOperatorHelper {
    MAX("max", "最大值"),
    MIN("min", "最小值"),
    AVG("avg", "平均值"),
    SUM("sum", "求和"),
    COUNT("count", "计数"),
    COUNT_DISTINCT("count distinct", "去重计数");

    private String code;
    private String desc;

    private static final Map<String, AggregationOperatorHelper> cached = new HashMap<>();

    static {
        Arrays.stream(AggregationOperatorHelper.values())
                .forEach(aoh -> cached.put(StringUtils.lowerCase(aoh.code), aoh));
    }

    AggregationOperatorHelper(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String toSql(String field) {
        String sqlSection = getCode() + "( ";
        if (getCode().equals(COUNT_DISTINCT.code)) {
            sqlSection = " COUNT( DISTINCT( ";
        }
        return sqlSection + field + ") ) AS " + field;
    }

    /**
     * 根据Code获取
     *
     * @param code String
     * @return AggregationOperatorHelper
     */
    public static AggregationOperatorHelper byCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return cached.get(StringUtils.lowerCase(code));
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
