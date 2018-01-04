package org.lv4j.framework.bean;

/**
 * 返回数据对象
 */
public class Data {
    /*1 模型数据*/
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
