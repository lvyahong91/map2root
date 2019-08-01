package org.lv4j.framework.helper;

import org.lv4j.framework.ConfigConstant;
import org.lv4j.framework.util.PropsUtils;

import java.util.Properties;

/**
 * 属性文件助手类
 */
public final class ConfigHelper {
    /*1 加载常量属性中设置的配置文件*/
    private static final Properties CONFIG_PROS= PropsUtils.loadProps(ConfigConstant.CONFIG_FILE);
    /*2 获取jdbc驱动*/
    public static String getJdbcDriver(){
        return PropsUtils.getString(CONFIG_PROS,ConfigConstant.JDBC_DRIVER);
    }
    /*3 获取JDBC连接串*/
    public static String getJdbcUrl(){
        return PropsUtils.getString(CONFIG_PROS,ConfigConstant.JDBC_URL);
    }
    /*4 获取JDBC用户名*/
    public static String getJdbcUserName(){
        return PropsUtils.getString(CONFIG_PROS,ConfigConstant.JDBC_USERNAME);
    }
    /*5 获取JDBC密码*/
    public static String getJdbcPassword(){
        return PropsUtils.getString(CONFIG_PROS,ConfigConstant.JDBC_PASSWORD);
    }

    /*6 获取应用基础包名*/
    public static String getAppBasePackage(){
        return PropsUtils.getString(CONFIG_PROS,ConfigConstant.App_BASE_PACKAGE);
    }
    /*7 获取应用JSP路径*/
    public static String getAppJspPath(){
        return PropsUtils.getString(CONFIG_PROS,ConfigConstant.APP_JSP_PATH);
    }
    /*8 获取应用静态资源路径*/
    public static String getAppAssetPath(){
        return PropsUtils.getString(CONFIG_PROS,ConfigConstant.APP_ASSET_PATH);
    }

}
