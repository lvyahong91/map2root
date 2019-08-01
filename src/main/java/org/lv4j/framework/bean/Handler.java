package org.lv4j.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装ACTION信息
 */
public class Handler {
    /*1 Controller类*/
    private Class<?> controllerClass;
    /*2 Action方法*/
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Handler() {
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
