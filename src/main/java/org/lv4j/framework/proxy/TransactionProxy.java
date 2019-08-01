package org.lv4j.framework.proxy;

import org.lv4j.framework.annatation.Transaction;
import org.lv4j.framework.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 事务代理
 */
public class TransactionProxy implements Proxy{
    private static final Logger LOGGER= LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER=new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    /**
     * 执行链式代理
     *
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result=null;
        boolean flag=FLAG_HOLDER.get();
        Method method=proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)){
            try {
                FLAG_HOLDER.set(true);
                DatabaseHelper.begintransaction();
                LOGGER.debug("begin transaction");
                result=proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("commit transaction");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                DatabaseHelper.rollBackTransaction();
                LOGGER.debug("rollback transaction");
            } finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result=proxyChain.doProxyChain();
        }
        return result;
    }
}
