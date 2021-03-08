package com.oneape.octopus.commons.value;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-06 15:05.
 * Modify:
 */
public class MathFormulaUtils {
    /**
     * Match floating-point Numbers.
     */
    public static final String FLOAT_NUMBER = "^(-?\\d+)(\\.\\d+)?$";

    /**
     * Check whether the mathematical formula is valid
     *
     * @param str String
     * @return boolean
     */
    public static boolean isValidMathFormula(String str) {
        try {
            calculate(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Computes the value of the specified formula.
     *
     * @param str String
     * @return String
     */
    public static String calculate(String str) {
        List<String> rpn = getRpn(str);
        return calculateRpn(rpn);
    }

    /**
     * Middle order after order.
     */
    private static List<String> getRpn(String str) {
        String tmp = StringUtils.trim(str);
        String[] formulaList = tmp.split(" ");

        Stack<String> operators = new Stack<>();
        List<String> result = new ArrayList<>();
        for (String formula : formulaList) {
            if (formula.matches(FLOAT_NUMBER)) {
                result.add(formula);
            } else if (formula.equals("(")) {
                operators.push(formula);
            } else if (formula.equals(")")) {
                while (!operators.peek().equals("(")) {
                    result.add(operators.pop());
                }
                operators.pop();
            } else {
                int level = getLevel(formula);
                if (level == -1 && !formula.matches(FLOAT_NUMBER)) {
                    throw new RuntimeException("Illegal digit:[" + formula + "]");
                }
                while (operators.size() != 0 && getLevel(operators.peek()) >= level) {
                    result.add(operators.pop());
                }
                operators.push(formula);
            }
        }
        while (operators.size() != 0) {
            result.add(operators.pop());
        }
        return result;
    }

    private static String calculateRpn(List<String> formulaList) {
        if (formulaList == null) {
            return null;
        }

        Stack<BigDecimal> s = new Stack<>();
        for (String formula : formulaList) {
            if (formula.matches(FLOAT_NUMBER)) {
                s.push(new BigDecimal(formula));
            } else {
                BigDecimal b = s.pop();
                BigDecimal a = s.pop();
                BigDecimal temp = ZERO;
                switch (formula) {
                    case "+":
                        temp = a.add(b);
                        break;
                    case "-":
                        temp = a.subtract(b);
                        break;
                    case "*":
                        temp = a.multiply(b);
                        break;
                    case "/":
                        temp = a.divide(b, 2, HALF_UP);
                        break;
                    case "max":
                        temp = a.max(b);
                        break;
                }
                s.push(temp);
            }
        }
        return s.pop().toString();
    }

    /**
     * Operator precedence.
     */
    private static int getLevel(String operator) {
        if (StringUtils.isBlank(operator)) {
            return 0;
        }
        switch (operator) {
            case "(":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
            case "max":
                return 2;
            default:
                return -1;
        }
    }
}
