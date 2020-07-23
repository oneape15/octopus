package com.oneape.octopus.parse.xml;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Operation type enumeration class
 * <p>
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-08 17:29.
 * Modify:
 */
public enum Operator {
    // Equal to
    EQUALS("eq"),
    // Is not equal to
    NOT_EQUALS("neq"),
    // Less than
    LESS_THAN("lt"),
    // Less than or equal to
    LESS_THAN_OR_EQUALS("le"),
    // Is greater than
    GREATER_THAN("gt"),
    // Greater than or equal to
    GREATER_THAN_OR_EQUALS("ge"),
    // Is empty
    BLANK("b"),
    // Don't empty
    NOT_BLANK("nb"),
    // Start with some string
    START_WITH("sw"),
    // End with some string
    END_WITH("ew"),
    // include
    INCLUDE("ic");

    private String code;

    private final static Map<String, Operator> map;


    static {
        map = new HashMap<>();
        for (Operator op : values()) {
            map.put(op.getCode(), op);
        }
    }

    Operator(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static Operator instance(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return map.get(StringUtils.lowerCase(code));
    }

    /**
     * The two parameters are compared according to the operator
     *
     * @param param1 the param 1
     * @param param2 The param 2
     * @return true - Conditions established; false - The condition does not hold
     */
    public boolean compare(Object param1, Object param2) {
        switch (this) {
            // Equal to
            case EQUALS:
                return optEqual(param1, param2);
            // Is not equal to
            case NOT_EQUALS:
                return optNotEqual(param1, param2);
            // Less than
            case LESS_THAN:
                return optLessThan(param1, param2);
            // Less than or equal to
            case LESS_THAN_OR_EQUALS:
                return optLessThanOrEquals(param1, param2);
            // Is greater than
            case GREATER_THAN:
                return optGreaterThan(param1, param2);
            // Greater than or equal to
            case GREATER_THAN_OR_EQUALS:
                return optGreaterThanOrEquals(param1, param2);
            // Is empty
            case BLANK:
                return optBlank(param1);
            // Don't empty
            case NOT_BLANK:
                return optNotBlank(param1);
            // Start with some string
            case START_WITH:
                return optStartWith(param1, param2);
            // End with some string
            case END_WITH:
                return optEndWith(param1, param2);
            // include
            case INCLUDE:
                return optInclude(param1, param2);
            default:
                return false;
        }
    }

    private static boolean optEqual(Object param1, Object param2) {
        if (param1 == param2) {
            return true;
        } else if (param1 == null || param2 == null) {
            return false;
        } else {
            boolean eq;
            if (param1 instanceof Object[] && param2 instanceof Object[]) {
                eq = deepEquals((Object[]) param1, (Object[]) param2);
            } else if (param1 instanceof byte[] && param2 instanceof byte[]) {
                eq = Arrays.equals((byte[]) param1, (byte[]) param2);
            } else if (param1 instanceof short[] && param2 instanceof short[]) {
                eq = Arrays.equals((short[]) param1, (short[]) param2);
            } else if (param1 instanceof int[] && param2 instanceof int[]) {
                eq = Arrays.equals((int[]) param1, (int[]) param2);
            } else if (param1 instanceof long[] && param2 instanceof long[]) {
                eq = Arrays.equals((long[]) param1, (long[]) param2);
            } else if (param1 instanceof char[] && param2 instanceof char[]) {
                eq = Arrays.equals((char[]) param1, (char[]) param2);
            } else if (param1 instanceof float[] && param2 instanceof float[]) {
                eq = Arrays.equals((float[]) param1, (float[]) param2);
            } else if (param1 instanceof double[] && param2 instanceof double[]) {
                eq = Arrays.equals((double[]) param1, (double[]) param2);
            } else if (param1 instanceof boolean[] && param2 instanceof boolean[]) {
                eq = Arrays.equals((boolean[]) param1, (boolean[]) param2);
            } else {
                // change to String compare.
                eq = StringUtils.equals(String.valueOf(param1), String.valueOf(param2));
            }
            return eq;
        }
    }

    public static boolean deepEquals(Object[] a1, Object[] a2) {
        if (a1 == a2)
            return true;
        if (a1 == null || a2 == null)
            return false;
        int length = a1.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            Object e1 = a1[i];
            Object e2 = a2[i];

            if (e1 == e2)
                continue;
            if (e1 == null)
                return false;

            // Figure out whether the two elements are equal
            boolean eq = optEqual(e1, e2);

            if (!eq)
                return false;
        }
        return true;
    }

    private static boolean optNotEqual(Object param1, Object param2) {
        return !optEqual(param1, param2);
    }

    /**
     * Compare param1 to param2.
     * If param1 or param2 is null then return false;
     *
     * @param param1 The param 1
     * @param param2 The param 2
     */
    private static boolean optLessThan(Object param1, Object param2) {
        if (param1 == null || param2 == null) return false;

        int status = toCompare(param1, param2);
        return status < 0;
    }

    private static boolean optLessThanOrEquals(Object param1, Object param2) {
        if (param1 == null || param2 == null) return false;

        int status = toCompare(param1, param2);
        return status <= 0;
    }

    private static boolean optGreaterThan(Object param1, Object param2) {
        if (param1 == null || param2 == null) return false;

        int status = toCompare(param1, param2);
        return status > 0;
    }

    private static boolean optGreaterThanOrEquals(Object param1, Object param2) {
        if (param1 == null || param2 == null) return false;

        int status = toCompare(param1, param2);
        return status >= 0;
    }

    private static boolean optBlank(Object param1) {
        return Objects.isNull(param1) || StringUtils.isBlank(String.valueOf(param1));
    }

    private static boolean optNotBlank(Object param1) {
        return !optBlank(param1);
    }

    private static boolean optStartWith(Object param1, Object param2) {
        return !((param1 == null || param2 == null))
                && StringUtils.startsWith(String.valueOf(param1), String.valueOf(param2));
    }


    private static boolean optEndWith(Object param1, Object param2) {
        return !((param1 == null || param2 == null))
                && StringUtils.endsWith(String.valueOf(param1), String.valueOf(param2));
    }

    private static boolean optInclude(Object param1, Object param2) {
        return !((param1 == null || param2 == null))
                && StringUtils.contains(String.valueOf(param1), String.valueOf(param2));
    }

    private static int toCompare(Object param1, Object param2) {
        // If one of the two arguments is of type String, it goes up to type String for comparison.
        if (param1 instanceof String || param2 instanceof String) {
            return Objects.compare(param1, param2, Comparator.comparing(p -> String.valueOf(p)));
        }

        // If the param is array.
        if (param1.getClass().isArray() && param2.getClass().isArray()) {
            return Objects.compare(param1, param2, Comparator.comparing(p -> Joiner.on(",").join((Object[]) p)));
        }

        String clz1 = param1.getClass().getName();
        String clz2 = param2.getClass().getName();

        // The type is BigDecimal or Double
        if (StringUtils.equals(clz1, BigDecimal.class.getName())
                || StringUtils.equals(clz2, BigDecimal.class.getName())
                || StringUtils.equals(clz1, Double.class.getName())
                || StringUtils.equals(clz2, Double.class.getName())
                ) {
            return Objects.compare(param1, param2, Comparator.comparingDouble(p -> Double.parseDouble(String.valueOf(p))));
        }

        if (StringUtils.equals(clz1, Long.class.getName()) || StringUtils.equals(clz2, Long.class.getName())) {
            return Objects.compare(param1, param2, Comparator.comparingLong(p -> Long.parseLong(String.valueOf(p))));
        }

        return Objects.compare(param1, param2, Comparator.comparingInt(p -> Integer.parseInt(String.valueOf(p))));
    }
}
