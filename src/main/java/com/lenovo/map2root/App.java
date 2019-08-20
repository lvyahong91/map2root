package com.lenovo.map2root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import antlr.collections.List;



/**
 * 处理出映射表，表结构为t_map2root(string basecode,string rootcode)
 *
 */
public class App 
{	
	private static String driverName="org.apache.hive.jdbc.HiveDriver";
	private static String url="jdbc:hive2://192.168.134.153:10000/mydb";
	private static String user="root";
	private static String password="root";
	
	private static Connection connection=null;
	private static Statement statement=null;
	private static ResultSet resultSet1=null;
	
	/**
	 * 创建hive驱动
	 * @throws Exception
	 */
	public void init() throws Exception{
		Class.forName(driverName);
		connection=DriverManager.getConnection(url, user, password);
		statement=connection.createStatement();
	}
	
	/**
	 * 创建映射表
	 * @throws Exception
	 */
	public void createTable() throws Exception {
		String sql = "create table proj_mec_dev.t_map2child(String basecode ,String rootcode) ;";
		System.out.println("Running: " + sql);
		statement.execute(sql);
	}
	
	/**
	 * 获得col_9的结果集
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getResult(String sql) throws SQLException {
		ResultSet resultSet2=null;
		System.out.println("Running: " + sql);
		resultSet2=statement.executeQuery(sql);
		return resultSet2;
	}
	
	public static void main(String[] args) throws SQLException {
		//将col_9的结果集处理成list
		ResultSet resultSet3=null;
		String sql1="select distinct col_9 from cdl_finance.bw_fin_zobpcm009_cdl;";
		java.util.List<String> list1=new ArrayList<String>();
		resultSet3=getResult(sql1);
		while (resultSet3.next()) {
			String col_9 = resultSet3.getString(1);
			list1.add(col_9);
		}
		System.out.print("list1的长度 = "+list1.size());
		//根据list1的数据去表里获取col_1的数据
		String sql2="select col_1 from cdl cdl_finance.bw_fin_zobpcm009_cdl where col_9="+list1.get(0);
		
		
		
		
	}

 
}
