package com.oneape.octopus.datasource;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    private static final Map<String, PeekRuleTypeHelper> cached = new HashMap<>();

    static {
        Arrays.stream(PeekRuleTypeHelper.values())
                .forEach(aoh -> cached.put(aoh.code.toLowerCase(), aoh));
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

    /**
     * 根据code进行查找
     *
     * @param code String
     * @return PeekRuleTypeHelper
     */
    public static PeekRuleTypeHelper byCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return cached.get(code.toLowerCase());
        }
        return null;
    }

    /**
     * 根据规则组装sql语句段
     *
     * @param dt        DataTye 数据类型
     * @param fieldName String  字段名称
     * @param value     String  值
     * @return String
     */
    public String toSqlSection(DataType dt, String fieldName, String value) {
        String section = "";
        switch (dt) {
            case STRING:
                switch (this) {
                    case EQUALS:
                    case EQUALS_IGNORE_CASE:
                        section = " = '" + value + "'";
                        break;
                    case NOT_EQUALS:
                        section = " != '" + value + "'";
                        break;
                    case START_WITH:
                        section = " LIKE '" + value + "%'";
                        break;
                    case END_WITH:
                        section = " LIKE '%" + value + "'";
                        break;
                    case CONTAINS:
                        section = " LIKE '%" + value + "%'";
                        break;
                    case INCLUDES:
                        section = " IN " + toIn(value, true);
                        break;
                    case NOT_INCLUDES:
                        section = " NOT IN " + toIn(value, true);
                        break;
                    default:
                        section = "";
                        break;
                }
                break;
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
                switch (this) {
                    case EQUALS:
                        section = " = " + value;
                        break;
                    case NOT_EQUALS:
                        section = " != " + value;
                        break;
                    case NO_LESS_THAN:
                        section = " >= " + value;
                        break;
                    case NO_MORE_THAN:
                        section = " <= " + value;
                        break;
                    case LESS_THAN:
                        section = " < " + value;
                        break;
                    case GREATER_THAN:
                        section = " > " + value;
                        break;
                    case INCLUDES:
                        section = " IN " + toIn(value, false);
                        break;
                    case NOT_INCLUDES:
                        section = " NOT IN " + toIn(value, false);
                        break;
                    default:
                        section = "";
                        break;
                }
                break;
            case DATE:
            case TIME:
            case DATETIME:
            case TIMESTAMP:
                switch (this) {
                    case EQUALS:
                        section = " = '" + value + "'";
                        break;
                    case GREATER_THAN:
                        section = " > '" + value + "'";
                        break;
                    case NO_MORE_THAN:
                        section = " <= '" + value + "'";
                        break;
                    case LESS_THAN:
                        section = " < '" + value + "'";
                        break;
                    case NO_LESS_THAN:
                        section = " >= '" + value + "'";
                        break;
                    default:
                        break;
                }
                break;
            case BOOLEAN:
            case BINARY:
            case OBJ:
            default:
                section = " = " + value;
                break;
        }
        return fieldName + section;
    }

    private static String toIn(String str, boolean needQuota) {
        String preSuffix = needQuota ? "'" : "";
        if (str == null) {
            return "(" + preSuffix + preSuffix + ")";
        }
        String[] arr = StringUtils.split(str, ",");
        return Arrays.stream(arr)
                .map(String::trim)
                .filter(StringUtils::isNoneEmpty)
                .map(item -> String.format("%s%s%s", preSuffix, item, preSuffix))
                .collect(Collectors.joining(",", "(", ")"));
    }

}
