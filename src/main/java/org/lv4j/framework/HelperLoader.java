package org.lv4j.framework;

import org.lv4j.framework.annatation.Controller;
import org.lv4j.framework.helper.*;
import org.lv4j.framework.util.ClassUtil;

/**
 * 加载相应的Helper
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] classList={
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls:classList){
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
