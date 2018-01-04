package org.lv4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 转型操作工具类
 */
public class CastUtil {

    /**
     * object转为string型
     * @param object
     * @return
     */
    public static String cast2String(Object object){
        return cast2String(object,"");
    }

    /**
     * 转为String型，可指定默认值
     * @param object
     * @param defaultValue
     * @return
     */
    private static String cast2String(Object object, String defaultValue) {
        return object != null ? String.valueOf(object):defaultValue;
    }

    /**
     * 转为浮点型，
     * @param object
     * @return
     */
    public static double cast2Double(Object object){
        return CastUtil.cast2Double(object,0);
    }

    /**
     * 转为浮点型，可指定默认值
     * @param object
     * @param defaultValue
     * @return
     */
    private static double cast2Double(Object object, double defaultValue) {
        double value=defaultValue;
        if (object != null){
            String strValue=cast2String(object);
            if (StringUtils.isNotEmpty(strValue)){
                try {
                    value=Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    value=defaultValue;
                }
            }
        }
        return value;
    }

    /**
     * 转为整数型
     * @param object
     * @return
     */
    public static int cast2Int(Object object) {
        return cast2Int(object,0);
    }

    /**
     * 转为整数型，可指定默认值
     * @param object
     * @param defaultValue
     * @return
     */
    private static int cast2Int(Object object, int defaultValue) {
        int value=defaultValue;
        if (object != null){
            String strValue=cast2String(object);
            if (StringUtils.isNotEmpty(strValue)){
                try {
                    value=Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    value=defaultValue;
                }
            }
        }
        return value;
    }

    /**
     * 转换为long
     * @param object
     * @return
     */
    public static long cast2Long(Object object){
        return cast2Long(object,0);
    }

    /**
     * 转换为long,可指定默认值
     * @param object
     * @param defaultValue
     * @return
     */
    private static long cast2Long(Object object, long defaultValue) {
        long value=defaultValue;
        if (object != null){
            String strValue=cast2String(object);
            if (StringUtils.isNotEmpty(strValue)){
                try {
                    value=Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    value=defaultValue;
                }
            }
        }
        return value;
    }

    /**
     * 转换为long
     * @param object
     * @return
     */
    public static Boolean cast2Boolean(Object object){
        return cast2Boolean(object,false);
    }

    /**
     * 转换为long,可指定默认值
     * @param object
     * @param defaultValue
     * @return
     */
    private static Boolean cast2Boolean(Object object, boolean defaultValue) {
        boolean value=defaultValue;
        if (object != null){
            String strValue=cast2String(object);
            if (StringUtils.isNotEmpty(strValue)){
                try {
                    value=Boolean.parseBoolean(strValue);
                } catch (NumberFormatException e) {
                    value=defaultValue;
                }
            }
        }
        return value;
    }
}
