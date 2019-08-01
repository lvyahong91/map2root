package org.lv4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.lv4j.framework.annatation.Inject;
import org.lv4j.framework.util.CollectionUtil;
import org.lv4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 */
public final class IocHelper {
    static {
        /*1 获取所有Bean类与实例之间的映射关系*/
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            //1.1 遍历beanMap
            for (Map.Entry<Class<?>,Object> beanEntry:beanMap.entrySet()){
                //1.2 从beanMap中获取Bean类与实例
                Class<?> beanClass=beanEntry.getKey();  //Bean类
                Object beanInstance=beanEntry.getValue(); //Bean实例
                //1.3 获取Bean类中所有的成员变量,包括public,private,protected
                Field[] fields=beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(fields)){
                    //1.4 遍历Bean Fields
                    for (Field field:fields){
                        //1.5 判断当前Beanfield 是否带有inject注解
                        if (field.isAnnotationPresent(Inject.class)){
                            //1.6 在Bean Map 获取当前BeanField对应的实例
                            Class<?> beanFieldClass=field.getType();
                            Object beanFiledInstance=beanMap.get(beanFieldClass);
                            if (beanFiledInstance !=null){
                                //1.7 通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance,field,beanFiledInstance);
                            }
                        }
                    }
                }
            }
        }
    }

}
