package org.lv4j.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流操作工具类
 */
public final class StreamUtil {
    /*1 从输入流中获取字符串*/
    public static String getString(InputStream is) throws IOException {
        StringBuilder sb=new StringBuilder();
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line=reader.readLine())!=null){
            sb.append(line);
        }
        return sb.toString();
    }


}
