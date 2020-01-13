package com.oneape.octopus.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * 取数规则类型
 */
public enum PeekRuleTypeHelper {
    EQUALS("equals", "等于"),
    EQUALS_IGNORE_CASE("equalsIgnoreCase", "等于(不分别大小写)"),
    NOT_EQUALS("notEquals", "不等于"),
    GREATER_THAN("greaterThan", "大于"),
    NO_MORE_THAN("notMoreThan", "不大于"),
    LESS_THAN("lessThan", "小于"),
    NO_LESS_THAN("notLessThan", "不小于"),
    CONTAINS("contains", "模糊匹配"),
    INCLUDES("includes", "包含"),
    NOT_INCLUDES("notIncludes", "不包含"),
    START_WITH("startWith", "以字符开始"),
    END_WITH("endWith", "以字符结束"),
    //
    ;

    private String code;
    private String desc;

    PeekRuleTypeHelper(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据数据类型获取支持的取数规则
     *
     * @param dt DataType
     * @return List
     */
    public static List<PeekRuleTypeHelper> getRuleByDataType(DataType dt) {
        List<PeekRuleTypeHelper> list = new ArrayList<>();
        switch (dt) {
            case STRING:
                list.add(EQUALS);
                list.add(EQUALS_IGNORE_CASE);
                list.add(NOT_EQUALS);
                list.add(START_WITH);
                list.add(END_WITH);
                list.add(INCLUDES);
                list.add(NOT_INCLUDES);
                list.add(CONTAINS);
                break;
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
                list.add(EQUALS);
                list.add(NOT_EQUALS);
                list.add(NO_LESS_THAN);
                list.add(NO_MORE_THAN);
                list.add(LESS_THAN);
                list.add(GREATER_THAN);
                list.add(INCLUDES);
                list.add(NOT_INCLUDES);
                break;
            case DATE:
            case TIME:
            case DATETIME:
            case TIMESTAMP:
                list.add(EQUALS);
                list.add(GREATER_THAN);
                list.add(NO_MORE_THAN);
                list.add(LESS_THAN);
                list.add(NO_LESS_THAN);
                break;
            case BOOLEAN:
            case BINARY:
            case OBJ:
            default:
                list.add(EQUALS);
                break;
        }
        return list;
    }

}
