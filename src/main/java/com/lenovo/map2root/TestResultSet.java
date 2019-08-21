package com.lenovo.map2root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestResultSet {
	private static String driverName="com.mysql.cj.jdbc.Driver";
	private static String url="jdbc:mysql://localhost:3306/cdl_finance?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8";
	private static String user="root";
	private static String password="root";
	
	private static Connection connection=null;
//	private static Statement statement=null;
	private static PreparedStatement ps=null;
	private static ResultSet resultSet=null;
	
	public static void main(String[] args) {
		java.util.List<String> list0=new ArrayList<String>();
		// TODO Auto-generated method stub
		try {
			Class.forName(driverName);
			connection=DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql="select * from cdl_finance.bw_fin_zobpcm009_cdl_1";
		try {
			ps=connection.prepareStatement(sql);
			System.out.println("Running: " + sql);
			resultSet=ps.executeQuery(sql);
			System.out.println("resultSet="+resultSet);
//			resultSet.last();
//			System.out.println("行数="+resultSet.getRow());
			
			while (resultSet.next()) {
				String col_9 = resultSet.getString(1);
				System.out.println("col9="+col_9);
				list0.add(col_9);
			}
			//根据list1的数据去表里获取col_1的数据,将结果转换为list
			ResultSet resultSet2=null;
			String sql2=
					"select col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9=?";
			ps=connection.prepareStatement(sql2);
			ps.setString(1,list0.get(1) );
			resultSet2=ps.executeQuery(sql2);
			System.out.println("Running: " + sql2);
			System.out.println("resultSet2="+resultSet2);
			while (resultSet2.next()) {
				String col_1 = resultSet2.getString(1);
				System.out.println("col1="+col_1);
//				list0.add(col_9);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
