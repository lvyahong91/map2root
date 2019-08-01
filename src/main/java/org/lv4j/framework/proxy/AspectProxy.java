package org.lv4j.framework.proxy;

import java.lang.reflect.Method;

/**
 * 切面代理
 */
public abstract class AspectProxy implements Proxy{

    public final Object doChain(ProxyChain proxyChain){
        Object result=null;
        Class<?> cls=proxyChain.getTargetClass();
        Method method=proxyChain.getTargetMethod();
        Object[] params=proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(cls,method,params)){
                before(cls,method,params);
                result=proxyChain.doProxyChain();
                after(cls,method,params);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {
            end();
        }
        return result;
    }

    public void end() {
    }

    public void after(Class<?> cls, Method method, Object[] params) {
    }

    public void before(Class<?> cls, Method method, Object[] params) {

    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) {
        return true;
    }

    public   void begin(){};
}
