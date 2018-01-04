package org.lv4j.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *解码与操作工具类
 */
public final class CodecUtil {
    /*1 将URL编码*/
    public static String encodeURL(String source){
        String target;
        try {
            target= URLEncoder.encode(source,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    /*2 将URL解码*/
    public static String decodeURL(String  source){
        String target;
        try {
            target= URLDecoder.decode(source,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return target;
    }
}
