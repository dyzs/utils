package com.dyzs.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String DATE_FORMAT_PATTERN_S_1 = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String DATE_FORMAT_PATTERN_S_2 = "yyyy-MM-dd HH:mm:ss";
    public static String getTimeForFileName() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN_S_2, Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN_S_1, Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN_S_1, Locale.CHINA);
        return format.format(new Date(time));
    }

    public static long getTimeMillis(String strTime, String formatPattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern, Locale.CHINA);
            Date date = format.parse(strTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
