package com.dyzs.udppaser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtils {
    public static final String DATE_FORMAT_PATTERN_S_1 = "yyyy-MM-dd HH:mm:ss SSS";
    public static String getTimeForFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
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
