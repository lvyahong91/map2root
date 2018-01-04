package org.lv4j.framework.helper;

import org.lv4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean助手类
 */
public class BeanHelper {

    /*1 定义bean映射,用于存放Bean类与Bean实例之间的映射关系*/
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>, Object>();
    static {
        Set<Class<?>> beanClassSet=ClassHelper.getBeanClassSet();
        for (Class<?> beanClass:beanClassSet){
            Object object= ReflectionUtil.newInstance(beanClass);  //动态获取类的实例
            BEAN_MAP.put(beanClass,object);
        }
    }

    /*2 获取bean映射*/
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /*3 获取bean实例*/
    public static <T>T getBean(Class<?> cls){  //<T>T  返回值类型
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class: "+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    public  static void setBean(Class<?> cls,Object object){
        BEAN_MAP.put(cls,object);
    }



}
