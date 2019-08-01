package org.lv4j.framework.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
    /*1 将字符串前面的空格进行处理  在判断字符串是否非空*/
    public static boolean isNotEmpty(String str){
        if (str != null){
            str=str.trim();
        }
        return StringUtils.isNotEmpty(str);
    }

    /*2 将字符串前面的空格进行处理，在判断字段是否为空*/
    public static boolean isEmpty(String str){
        if (str !=null){
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static String[] splitString(String body, String s) {
        String[] lines;
        lines=body.split("s");
        return lines;
    }
}
