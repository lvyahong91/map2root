package org.lv4j.framework.util;

import org.slf4j.impl.Log4jLoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 属性文件工具类
 */
public final class PropsUtils {
    Log4jLoggerFactory log4jLoggerFactory=new Log4jLoggerFactory();
    private static final Logger LOGGER=null;

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName){
        Properties properties=null;
        InputStream is=null;
        try {
            is=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);  //将文件用流的形式进行读取
            if (is == null){
                throw  new FileNotFoundException(fileName+"file is not found");
            }
            properties=new Properties();
            properties.load(is);
        } catch (FileNotFoundException e) {   //用日志定位异常 ，待改进
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 获取字符型属性，默认值为0
     */
    public static String getString(Properties properties,String key){
        return getString(properties,key,"");
    }

    /**
     * 获取字符型属性，可指定默认值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    private static String getString(Properties properties, String key, String defaultValue) {
        String value=defaultValue;
        if (properties.containsKey(key)){
            value=properties.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值型属性，默认值为0
     */
    public static int getInt(Properties properties,String key){
        return getInt(properties,key,0);
    }

    /**
     * 获取数值型属性,可指定默认值
     * @param properties
     * @param key
     * @param defaultValue
     */
    private static int getInt(Properties properties, String key, int defaultValue) {
        int value=defaultValue;
        if (properties.containsKey(key)){
            value=CastUtil.cast2Int(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取bool型属性
     * @param properties
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties properties,String key){
        return getBoolean(properties,key,false);
    }

    /**
     * 获取bool型属性,可指定默认值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    private static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value=defaultValue;
        if (properties.containsKey(key)){
            value=CastUtil.cast2Boolean(properties.getProperty(key));
        }
        return value;
    }


}
