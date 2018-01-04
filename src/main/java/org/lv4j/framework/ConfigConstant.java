package org.lv4j.framework;

/**
 * 提供相关配置的常量
 */
public interface ConfigConstant {

    String CONFIG_FILE="lv4j.proprerties";  //配置文件的名称
    String JDBC_DRIVER="lv4j.framework.jdbc.driver";  //jdbc驱动
    String JDBC_URL="lv4j.framework.jdbc.url";  //驱动的模式，具体连接那种数据库
    String JDBC_USERNAME="lv4j.framework.jdbs.username";  //数据库的用户名
    String JDBC_PASSWORD="lv4j.framework.jdbc.password";  //数据库的用户密码

    String App_BASE_PACKAGE="lv4j.framework.app.base_package";  //项目的基础包名
    String APP_JSP_PATH="lv4j.framework.app.jsp_path";  //jsp的基础路径
    String APP_ASSET_PATH="lv4j.framework.app.asset_path";  //js,html,图片等静态资源的基础路径

}
