package org.lv4j.framework.bean;

import org.lv4j.framework.util.CastUtil;
import org.lv4j.framework.util.ClassUtil;

import java.util.Map;

/**
 * 请求参数对象
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    /*3 根据参数名获取long 型参数值*/
    public long getLong(String name){
        return CastUtil.cast2Long(paramMap.get(name));
    }

    /*4 获取所有的字段信息*/
    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
