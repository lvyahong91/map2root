package org.lv4j.framework.helper;

import org.lv4j.framework.annatation.Controller;
import org.lv4j.framework.annatation.Service;
import org.lv4j.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 */
public final class ClassHelper {
    /*1 定义类集合，用于存放所加载的类*/
    private static final Set<Class<?>> CLASS_SET;
    static {
        String basePackage=ConfigHelper.getAppBasePackage();
        CLASS_SET= ClassUtil.getClassSet(basePackage);
    }

    /*2 获取应用包名下所有的类*/
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /*3 获取应用包名下所有的Service类*/
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for (Class<?> cls:classSet){
            if (cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /*4 获取应用包名下所有的Controller类*/
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for (Class<?> cls:classSet){
            if (cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /*5 获得应用包名下所有的Bean类，包括Service,Controller*/
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        classSet.addAll(getControllerClassSet());
        classSet.addAll(getServiceClassSet());
        return classSet;
    }

    /*6 获取应用包名下某父类（或接口）的所有子类（或实现类）*/
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for (Class<?> cls:CLASS_SET){
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /*7 获取应用包名下带有某注解的类*/
    public static Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotationClass){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for (Class<?> cls:CLASS_SET){
            if (cls.isAnnotationPresent(annotationClass) ){
                classSet.add(cls);
            }
        }
        return classSet;
    }

}
