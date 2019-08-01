package org.lv4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库操作助手类
 */
public final class DatabaseHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER=new ThreadLocal<Connection>(){
        @Override
        protected Connection initialValue() {
            return null;
        }
    };
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String url="jdbc:mysql://localhost:3306/demo";
    private static final String username="root";
    private static final String password="root";

    /*1 开启事务*/
    public static void begintransaction() {
        Connection conn=getConnection();
        if (conn != null){
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    /* 获取连接*/
    private static Connection getConnection() {
        Connection conn=CONNECTION_HOLDER.get();
        try {
            if (conn == null){
                Class.forName(driver);
                conn= DriverManager.getConnection(url,username,password);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            CONNECTION_HOLDER.set(conn);
        }
        return conn;
    }

    /* 关闭连接*/
    private static void closeConnection(){
        Connection conn=CONNECTION_HOLDER.get();
        try {
            if (conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CONNECTION_HOLDER.remove();
        }
    }

    /*2 提交事务*/
    public static void commitTransaction() {
        Connection conn=getConnection();
        try {
            if (conn != null){
                conn.commit();
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error("commit transaction failure",e);
            throw new RuntimeException(e);
        } finally {
            CONNECTION_HOLDER.remove();
        }
    }

    /*3 回滚事务*/
    public static void rollBackTransaction(){
        Connection conn=getConnection();
        try {
            if (conn != null){
                conn.rollback();
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error("rollback transaction failure",e);
            throw new RuntimeException(e);
        } finally {
            CONNECTION_HOLDER.remove();
        }
    }

}
