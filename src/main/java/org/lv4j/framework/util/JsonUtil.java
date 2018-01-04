package org.lv4j.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Json操作类
 */
public final class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();

    /*1 将POJO转换为JSON*/
    public static <T> String toJson(T object){
        String json;
        try {
            json=OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    /*2 将JSON转换为POJO*/
    public static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try {
            pojo=OBJECT_MAPPER.readValue(json,  type);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
        return pojo;
    }

}
