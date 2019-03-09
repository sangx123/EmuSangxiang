package com.xiang.one.utils;

import java.util.regex.Pattern;

public class RegexMatcher {

    private static final Pattern phoneNumPattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
    private static final Pattern phoneNumPattermHk = Pattern.compile("^([5|6|8|9])\\d{7}$");
    private static final Pattern phoneNumPatternMC = Pattern.compile("^(6[6|8])\\d{5}$");
    private static final Pattern emailPattern = Pattern.compile("^[a-z0-9A-Z][a-z0-9A-Z_\\.-]+@([a-z0-9A-Z_-]+\\.)+[a-zA-Z]{2,}$");

    public static boolean isPhoneNumber(String str) {
        return phoneNumPattern.matcher(str).matches() || phoneNumPattermHk.matcher(str).matches() || phoneNumPatternMC.matcher(str).matches();
    }

    public static boolean isEmail(String str) {
        return emailPattern.matcher(str).matches();
    }
}
