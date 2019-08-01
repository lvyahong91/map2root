import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLib 代理类
 */
public class CGLibProxy implements MethodInterceptor {
    public <T> T getProxy(Class<?> cls){
        return (T) Enhancer.create(cls,this);
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        before();
        Object result=methodProxy.invokeSuper(object,args);
        after();
        return result;
    }

    private void before() {
        System.out.println("before");
    }

    private void after(){
        System.out.println("after");
    }

    public static void main(String[] args){
        CGLibProxy cgLibProxy=new CGLibProxy();
        Hello helloProxy=cgLibProxy.getProxy(HelloImpl.class);
        helloProxy.sayHello("Neo");
    }
}
