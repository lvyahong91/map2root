package testThreadLocal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟线程局部变量容器
 */
public class MyThreadLocal<T> {
    private Map<Thread,T> container= Collections.synchronizedMap(new HashMap<Thread, T>());

    /*1 给当前线程设置局部变量*/
    public void set(T value){
        container.put(Thread.currentThread(),value);
    }

    /*2 从当前线程取得局部变量*/
    public T get(){
        Thread thread=Thread.currentThread();
        T value=container.get(thread);
        if (value == null && !container.containsKey(thread) ){
            value=initValue();
            container.put(thread,value);
        }
        return value;
    }

    protected T initValue() {
        return null;
    }

    public void remove(){
        container.remove(Thread.currentThread());
    }
}
