package com.dyzs.heheda;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.CHINA);
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.CHINA);
        return format.format(new Date(time));
    }
}
