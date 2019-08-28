package com.lenovo.map2root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;





/**
 * 处理出映射表，表结构为t_map2root(string basecode,string rootcode)
 *
 */
public class App4 
{	
	private static String driverName="com.mysql.cj.jdbc.Driver";
	private static String url="jdbc:mysql://localhost:3306/cdl_finance?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8";
	private static String user="root";
	private static String password="root";
	
	private static Connection connection=null;
//	private static Statement statement=null;
	private static PreparedStatement ps=null;
	private static ResultSet resultSet=null;
	private static Map<String,String> map=null;
	
	/**
	 * 创建hive驱动
	 * @throws Exception
	 */
	public static void init() throws Exception{
		try {
			Class.forName(driverName);
			connection=DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 创建映射表
	 * @throws Exception
	 */
	
	  public static void updateTable(String basecode,String rootcode) throws Exception {
		  String sql="insert into cdl_finance.t_map2root(memberid,parenth9) values (?,?)";
		  ps=connection.prepareStatement(sql);
		  ps.setString(1, basecode);
		  ps.setString(2, rootcode);
		  System.out.println("Running: " + sql);
		  ps.executeUpdate();
		  
	  }
	 
	
	/**
	 * 根据sql获取结果集
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getResult(String sql) throws SQLException {
		//预防sql注入，使用预编译sql
		ps=connection.prepareStatement(sql);
		System.out.println("Running: " + sql);
		resultSet=ps.executeQuery(sql);
//		System.out.println("resultSet="+resultSet);
//		resultSet.last();
//		System.out.println("行数="+resultSet.getRow());
		return resultSet;
	}
	/**
	 * 将结果集转换为列表
	 * @param sql1
	 * @return
	 * @throws SQLException
	 */
	private static java.util.List<String> getList(String sql1) throws SQLException {
		try {
			//		ResultSet resultSet3=null;
			java.util.List<String> list = new ArrayList<String>();
			resultSet = getResult(sql1);
			while (resultSet.next()) {
				String col_9 = resultSet.getString(1);
				System.out.println("col9=" + col_9);
				list.add(col_9);
			}
			return list;
		} finally {
			resultSet.close();
			ps.close();
			
		}
	}
	/**
	 * 查找rootcode的逻辑为根据parent列的code,过滤col_1的memberid,以此为第一重过滤
	 * 第二重，遍历第一重的memberid，若是在对应的parent列查找不到相同信息，该memberid就是basecode
	 * 若是在parent列能查找到相同的memberid,则需要以此memberid为parent的过滤条件，将过滤后的memberid列
	 * 重新作为第二重的条件，如此递归
	 * @throws Exception 
	 */
	public static void getBaseCode() throws Exception {
		try {
			//获得col_9的结果集，将结果集转换为list
			String sql1 = "select distinct col_11 from cdl_finance.bw_fin_zobpcm009_cdl_1";
			java.util.List<String> list0 = getList(sql1);
			System.out.println("list0的长度 = " + list0.size());
			//根据list1的数据去表里获取col_1的数据,将结果转换为list
			/*
			 * String sql2=
			 * "select col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_11='"+list0.
			 * get(9) +"'";
			 */
			String sql2 = "select col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_11='LPVC'";
			java.util.List<String> list1 = getList(sql2);
			System.out.println("list1的长度 = " + list1.size());
			System.out.println("此处为第一重col_1的值");
			
			
				getFinalBase(list0, list1);
			
		} finally {
			
			
			connection.close();
		}
		
		
	}
	/**
	 * 采用递归函数，循环调用本函数找出rootcode
	 * @param list0
	 * @param list1
	 * @throws Exception 
	 */
	public static void getFinalBase(List<String> list0,List<String> list1) throws Exception {		
		int cnt=0;
		for(int i=0;i<list1.size();i++) {
			//第二重过滤，是根据list1的memberid作为条件，取得新的memberid列
			String sql2=
					"select  col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_11='"+list1.get(i) +"'";
			List<String> list2=getList(sql2);
			System.out.println("list2的长度 = "+list2.size());
	
			//将list2的数据和list0的值进行比对，若没有相同的，则list2的数据就是basecode
			//若有相同的数据，需要将该数据作为从list0的过滤条件，重新得到新的list2
//			ListIterator<String> it=list2.listIterator();
			for (String col_1 : list2) {
				System.out.println("list2的迭代"+col_1);
				if (list0.contains(col_1)) 
				{ //此处条件待修正
					System.out.println("此处list2进入遍历");
					System.out.println("此处为开始第三重过滤的col_1的值");
					
					getFinalBase(list0,list2);//递归调用本函数，直至找到basecode				
				}
				else if (! list0.contains(col_1) ) {
					if (col_1 != null) {
						System.out.println("此处为第二重list_col_1的遍历");
						System.out.println("basecode ="+col_1);
						System.out.println("parentcode ="+"LPVC");
						updateTable(col_1, "LPVC");
						
					} else {
						System.out.println("list2为空，上层list1.get(i)已经找到，可以跳出循环");
						System.out.println("basecode ="+list1.get(i));
						System.out.println("parentcode ="+"LPVC");
						updateTable(list1.get(i), "LPVC");
					}
				}				
			}
			System.out.println("内层for循环结束"+cnt+1);
			continue;
			
		}	
		
	}

	public static void main(String[] args) throws Exception {
		System.out.println("连接打开="+connection);
		init();
		System.out.println("连接打开="+connection);
		getBaseCode();
	}

 
}
