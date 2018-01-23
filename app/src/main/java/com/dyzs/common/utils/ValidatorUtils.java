package com.dyzs.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maidou on 2017/11/16.
 */

public class ValidatorUtils {
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w -\\./?%&=#]*)?";
    public static final String REGEX_TB_FIRST_KEY_WORD = "v.cvz([a-zA-Z0-9-\\./?%&=#]*)\\.com";

    public static String charSeparator(String str, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 校验正则表达式，只能做简单的校验
     * @return 校验通过返回true，否则返回false
     */
    public static boolean checkRegex(String str, String regex) {
        return Pattern.matches(regex, str);
    }

}
