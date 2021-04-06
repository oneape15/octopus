package com.oneape.octopus.commons.value;

import com.oneape.octopus.commons.enums.DateType;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * Patterns
     */
    public static final String DAY_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final Date DEFAULT_DATE = DateUtils.parseByDayPattern("1970-01-01");

    /**
     * Parse date by 'yyyy-MM-dd' pattern
     *
     * @param str String
     * @return Date
     */
    public static Date parseByDayPattern(String str) {
        return parseDate(str, DAY_PATTERN);
    }

    /**
     * Parse date by 'yyyy-MM-dd HH:mm:ss' pattern
     *
     * @param str String
     * @return Date
     */
    public static Date parseByDateTimePattern(String str) {
        return parseDate(str, DATETIME_PATTERN);
    }

    /**
     * Parse date without Checked exception
     *
     * @param str     String
     * @param pattern String
     * @return Date
     * @throws RuntimeException when ParseException occurred
     */
    public static Date parseDate(String str, String pattern) {
        try {
            return parseDate(str, new String[]{pattern});
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Format date into string
     *
     * @param date    Date
     * @param pattern String
     * @return String
     */
    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * Format date by 'yyyy-MM-dd' pattern
     *
     * @param date Date
     * @return String
     */
    public static String formatByDayPattern(Date date) {
        if (date != null) {
            return DateFormatUtils.format(date, DAY_PATTERN);
        } else {
            return null;
        }
    }

    /**
     * Format date by 'yyyy-MM-dd HH:mm:ss' pattern
     *
     * @param date Date
     * @return String
     */
    public static String formatByDateTimePattern(Date date) {
        return DateFormatUtils.format(date, DATETIME_PATTERN);
    }

    /**
     * Get current day using format date by 'yyyy-MM-dd HH:mm:ss' pattern
     *
     * @return String
     */
    public static String getCurrentDayByDayPattern() {
        Calendar cal = Calendar.getInstance();
        return formatByDayPattern(cal.getTime());
    }

    /**
     * Gets the chain rate date based on the date type and the current date
     *
     * @param dt         DateType
     * @param dateStr    String
     * @param dateformat String
     * @return String
     */
    public static String getChainRateDate(DateType dt, String dateStr, String dateformat) {
        Date currentDate = parseDate(dateStr, dateformat);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        switch (dt) {
            case HOUR:
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                break;
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case WEEK:
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, -1);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, -1);
                break;
        }
        return formatDate(calendar.getTime(), dateformat);
    }

    /**
     * Gets the year-over-year date based on the date type and the current date
     *
     * @param dt         DateType
     * @param dateStr    String
     * @param dateformat String
     * @return String
     */
    public static String getYoyDate(DateType dt, String dateStr, String dateformat) {
        Date currentDate = parseDate(dateStr, dateformat);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        switch (dt) {
            case HOUR:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case WEEK:
                calendar.add(Calendar.WEEK_OF_MONTH, -4);
                break;
            case MONTH:
                calendar.add(Calendar.YEAR, -1);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, -36);
                break;
        }
        return formatDate(calendar.getTime(), dateformat);
    }
}
