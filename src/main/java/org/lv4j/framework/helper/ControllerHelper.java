package org.lv4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.lv4j.framework.annatation.Action;
import org.lv4j.framework.bean.Handler;
import org.lv4j.framework.bean.Request;
import org.lv4j.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 */
public final class ControllerHelper {
    /*1 用于存放请求与处理器的映射关系(简称Action Map)*/
    private static final Map<Request,Handler> ACTION_MAP=new HashMap<Request, Handler>();
    static {
        //1.1 获取所有的Controller类
        Set<Class<?>> controllerClassSet=ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)){
            //1.2 遍历这些Controller类
            for (Class<?> controllerClass:controllerClassSet){
                //1.3 获取controller类定义的方法
                Method[] methods=controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)){
                    //1.4 遍历这些类中的方法
                    for (Method method:methods){
                        //1.5 判断当前方法是否带有Action注解
                        if (method.isAnnotationPresent(Action.class)){
                            //1.6 从Action注解中获取URL映射规则
                            Action action=method.getAnnotation(Action.class);
                            String mapping=action.value();
                            //1.7 验证映射规则
                            if (mapping.matches("\\w+:/\\w*")){
                                String[] array=mapping.split(":");
                                if (ArrayUtils.isNotEmpty(array) && array.length==2){
                                    //1.8 获取请求方法与路径
                                    String requestMethod=array[0];
                                    String requestPath=array[1];
                                    Request request=new Request(requestMethod,requestPath);
                                    Handler handler=new Handler(controllerClass,method);
                                    //1.9 初始化Action Map
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*2 获取handler*/
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request=new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }

}
