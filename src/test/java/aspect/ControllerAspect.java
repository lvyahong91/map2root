package aspect;

import org.lv4j.framework.annatation.Aspect;
import org.lv4j.framework.annatation.Controller;
import org.lv4j.framework.proxy.AspectProxy;
import org.lv4j.framework.proxy.ProxyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 拦截Controller所有方法
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy{
    private static final Logger LOGGER= LoggerFactory.getLogger(ControllerAspect.class);
    private long begin;

    public ControllerAspect(long begin) {
        this.begin = begin;
    }


    @Override
    public void before(Class<?> cls, Method method, Object[] params) {
        LOGGER.debug("----------------------begin------------------------");
        LOGGER.debug(String.format("class: %s",cls.getName()));
        LOGGER.debug(String.format("method: %s",method.getName()));
        begin=System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) {
        LOGGER.debug(String.format("time: %dms",System.currentTimeMillis()-begin));
        LOGGER.debug("-----------------------end--------------------------------");
    }


    /**
     * 执行链式代理
     *
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        return null;
    }
}
