package com.example.hardcodefinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static String REGEX_SET_CONTENT = "\\.(setContent||setContentTitle||setTitle||setContentText)\\(\"(.)*[\\u4e00-\\u9fa5]*(.)*\"\\)";
    public static String XML_ANDROID_TEXT = "android\\:text\\=\"(.)*[\\u4e00-\\u9fa5](.)*\"";

    public static String REGEX_CHECK_CH = "[\\u4e00-\\u9fa5]";

    public static String stringFilter(String regex, String str) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return m.group(0);
        }
        return m.replaceAll("").trim();
    }

    public static boolean stringFilterMatch(String regex, String str) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String[] stringFilters(String regex, String str) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        int groupCount = m.groupCount();
        String[] matches = new String[groupCount];
        while (m.find()) {
            if (groupCount > 0) {
                for (int i = 0; i < groupCount; i++) {
                    matches[i] = m.group(i);
                }
            }
        }
        return matches;
    }

    public static String gotTheChineseString(String str) {
        Pattern p = Pattern.compile(REGEX_CHECK_CH);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return m.group();
        }
        return "";
    }

    public static boolean gotTheChinese(String str) {
        Pattern p = Pattern.compile(REGEX_CHECK_CH);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
