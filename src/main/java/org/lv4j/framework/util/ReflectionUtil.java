package org.lv4j.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public final class ReflectionUtil {

    /*1 创建实例*/
    public static Object  newInstance(Class<?> cls){
        Object instance=null;
        try {
            instance=cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    /*2 调用方法*/
    public static Object invokeMethod(Object object, Method method,Object...args){
        Object result=null;
        try {
            method.setAccessible(true);
            result=method.invoke(object,args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /*3 设置成员变量的值*/
    public static void setField(Object object, Field field,Object value){
        try {
            field.setAccessible(true);
            field.set(object,value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
