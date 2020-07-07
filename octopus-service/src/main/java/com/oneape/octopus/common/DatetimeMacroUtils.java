package com.oneape.octopus.common;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.commons.value.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Datetime macro parser.
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-07 16:37.
 * Modify:
 */
@Slf4j
public final class DatetimeMacroUtils {

    /**
     * The macro rule enum
     */
    public enum Rule {
        SET,
        ADD;

        public static Rule getByName(String name) {
            for (Rule r : values()) {
                if (StringUtils.equalsIgnoreCase(name, r.name())) {
                    return r;
                }
            }
            return null;
        }
    }

    /**
     * The macro unit enum
     */
    public enum Unit {
        YEARS,
        MONTHS,
        DAYS,
        QUARTERS,
        WEEKS;

        public static Unit getByName(String name) {
            for (Unit unit : values()) {
                if (StringUtils.equalsIgnoreCase(name, unit.name())) {
                    return unit;
                }
            }
            return null;
        }
    }

    /**
     * The macro tag enum
     */
    public enum Tag {
        BEGIN,
        END;

        public static Tag getByName(String name) {
            for (Tag tag : values()) {
                if (StringUtils.equalsIgnoreCase(name, tag.name())) {
                    return tag;
                }
            }
            return null;
        }
    }

    /**
     * Parser the datetime macro string.
     * <p>
     * ADD 1 DAYS     Add one day to the current date
     * ADD 1 WEEKS    Add one week to the current date
     * ADD 1 MONTHS   Add one month to the current date
     * ADD 1 QUARTERS Add one quarters to the current date
     * ADD 1 YEARS    Add one year to the current date
     * ADD -1 DAYS    Reduce the current date by one day
     * <p>
     * SET 1  WEEKS        Set the first day of next week
     * SET 1  MONTHS       Set the first day of the next month
     * SET -1 MONTHS       Set the first day of last month
     * SET -1 MONTHS BEGIN Set the first day of last month
     * SET -1 MONTHS END   Set the last day of the month
     *
     * @param macroStr      the macro string to parse, not null
     * @param parsePatterns String the date format patterns to use, see SimpleDateFormat, not null.
     * @return the parsed date
     */
    public static Date parserMacroValue(String macroStr,
                                        String parsePatterns) {
        Preconditions.checkArgument(StringUtils.isNotBlank(macroStr), "datetime macro string is null.");
        Preconditions.checkArgument(StringUtils.isNotBlank(parsePatterns), "date format patterns is null.");

        // Delimit the string by space
        String[] macro = StringUtils.split(StringUtils.upperCase(StringUtils.trimToEmpty(macroStr)), " ");


        if (macro == null || (macro.length != 3 && macro.length != 4)) {
            // May be a fixed date
            try {
                return DateUtils.parseDate(macroStr, parsePatterns);
            } catch (Exception e) {
                log.warn("parse date string error, value: {}, parsePatterns: {}", macroStr, parsePatterns);
            }

            return null;
        }

        Rule rule = Rule.getByName(macro[0]);
        Integer offset = DataUtils.toInt(macro[1], null);
        Unit unit = Unit.getByName(macro[2]);
        if (rule == null || offset == null || unit == null) {
            log.error("Invalid datetime macro string.");
            return null;
        }
        Tag tag = null;
        if (macro.length == 4) {
            tag = Tag.getByName(macro[3]);
        }

        switch (rule) {
            case ADD:
                return addOption(unit, offset);
            case SET:
                return setOption(unit, tag, offset);
            default:
                return null;
        }
    }

    private static Date addOption(Unit unit, Integer offset) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        switch (unit) {
            case DAYS:
                calendar.add(Calendar.DAY_OF_MONTH, offset);
                break;
            case WEEKS:
                calendar.add(Calendar.WEEK_OF_MONTH, offset);
                break;
            case MONTHS:
                calendar.add(Calendar.MONTH, offset);
                break;
            case QUARTERS:
                calendar.add(Calendar.MONTH, offset * 3);
                break;
            case YEARS:
                calendar.add(Calendar.YEAR, offset);
                break;
            default:
                return null;
        }
        return calendar.getTime();
    }

    private static Date setOption(Unit unit, Tag tag, Integer offset) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        boolean isEndDay = tag != null && tag == Tag.END;

        switch (unit) {
            case DAYS:
                calendar.add(Calendar.DAY_OF_MONTH, offset);
                break;
            case WEEKS:
                calendar.add(Calendar.WEEK_OF_MONTH, offset);
                if (isEndDay) {
                    // Offset to Sunday
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                } else {
                    // Offset to Monday
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                }
                break;
            case MONTHS:
                calendar.add(Calendar.MONTH, offset);
                if (isEndDay) {
                    // Gets the maximum number of days for a month
                    int lastDay = calendar.getActualMaximum(Calendar.DATE);

                    // The last day of the offset month
                    calendar.set(Calendar.DAY_OF_MONTH, lastDay);
                } else {
                    // The first day of the offset month
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case QUARTERS:
                int curMonth = calendar.get(Calendar.MONTH) + 1;
                int curQrt = curMonth / 3 + (curMonth % 3 == 0 ? 0 : 1);
                int plus = offset * 3;

                if (isEndDay) {
                    calendar.set(Calendar.MONTH, curQrt * 3 - 1);
                    calendar.add(Calendar.MONTH, plus);
                    // Gets the maximum number of days for a month
                    int lastDay = calendar.getActualMaximum(Calendar.DATE);

                    // The last day of the offset month
                    calendar.set(Calendar.DAY_OF_MONTH, lastDay);
                } else {
                    calendar.set(Calendar.MONTH, curQrt * 3 - 2 - 1);
                    calendar.add(Calendar.MONTH, plus);
                    // The first day of the offset month
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case YEARS:
                calendar.add(Calendar.YEAR, offset);
                if (isEndDay) {
                    calendar.set(Calendar.MONTH, 11);
                    // Gets the maximum number of days for a month
                    int lastDay = calendar.getActualMaximum(Calendar.DATE);

                    // The last day of the offset month
                    calendar.set(Calendar.DAY_OF_MONTH, lastDay);
                } else {
                    // The first month of the offset year
                    calendar.set(Calendar.MONTH, 0);
                    // The first day of the first month of the offset year
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            default:
                return null;
        }

        return calendar.getTime();
    }

}
