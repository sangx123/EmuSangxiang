package com.sangxiang.android.utils;

public class StringUtils {
    public static String floatToString(Float f){
        String regex = "(\\d+)(?:(\\.\\d*[^0])|\\.)0*";
        return (f+"").replaceAll(regex, "$1$2").replace("null","");
    }
}
