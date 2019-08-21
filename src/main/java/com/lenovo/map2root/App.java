package com.lenovo.map2root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;





/**
 * 处理出映射表，表结构为t_map2root(string basecode,string rootcode)
 *
 */
public class App 
{	
	private static String driverName="com.mysql.cj.jdbc.Driver";
	private static String url="jdbc:mysql://localhost:3306/cdl_finance?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8";
	private static String user="root";
	private static String password="root";
	
	private static Connection connection=null;
//	private static Statement statement=null;
	private static PreparedStatement ps=null;
	private static ResultSet resultSet=null;
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
	/*
	 * @SuppressWarnings("null") public void createTable() throws Exception {
	 * Statement statement1=null; String sql =
	 * "create table proj_mec_dev.t_map2child(String basecode ,String rootcode) ;";
	 * System.out.println("Running: " + sql); statement1.execute(sql); }
	 */
	
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
		System.out.println("resultSet="+resultSet);
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
		ResultSet resultSet3=null;
		java.util.List<String> list=new ArrayList<String>();
		resultSet3=getResult(sql1);
		while (resultSet3.next()) {
			String col_9 = resultSet3.getString(1);
			System.out.println("col9="+col_9);
			list.add(col_9);
		}
		return list;
	}
	/**
	 * 查找rootcode的逻辑为根据parent列的code,过滤col_1的memberid,以此为第一重过滤
	 * 第二重，遍历第一重的memberid，若是在对应的parent列查找不到相同信息，该memberid就是basecode
	 * 若是在parent列能查找到相同的memberid,则需要以此memberid为parent的过滤条件，将过滤后的memberid列
	 * 重新作为第二重的条件，如此递归
	 * @throws SQLException
	 */
	public static void getBaseCode() throws SQLException {
		//获得col_9的结果集，将结果集转换为list
		String sql1="select distinct col_9 from cdl_finance.bw_fin_zobpcm009_cdl_1";
		java.util.List<String> list0 = getList(sql1);
		System.out.println("list0的长度 = "+list0.size());
		//根据list1的数据去表里获取col_1的数据,将结果转换为list
		/*
		 * String sql2=
		 * "select col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9='"+list0.
		 * get(9) +"'";
		 */
		String sql2=
				"select col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9='LPBUWT'";
		java.util.List<String> list1=getList(sql2);
		System.out.println("list1的长度 = "+list1.size());
		getFinalBase(list0,list1);
		
	}
	/**
	 * 采用递归函数，循环调用本函数找出rootcode
	 * @param list0
	 * @param list1
	 * @throws SQLException
	 */
	public static void getFinalBase(java.util.List<String> list0,java.util.List<String> list1) throws SQLException {
		for(int i=0;i<list1.size();i++) {
			//第二重过滤，是根据list1的memberid作为条件，取得新的memberid列
			String sql2=
					"select  col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9='"+list1.get(i) +"'";
			java.util.List<String> list2=getList(sql2);
			System.out.println("list2的长度 = "+list2.size());
			if (list2.size() ==0) {
				System.out.println("不需要再往下找");
				System.out.println("basecode ="+list1.get(i));
				System.out.println("parentcode ="+"LPBUWT");
				continue;
			}
			//将list2的数据和list0的值进行比对，若没有相同的，则list2的数据就是basecode
			//若有相同的数据，需要将该数据作为从list0的过滤条件，重新得到新的list2
			ListIterator<String> it=list2.listIterator();
			while(it.hasNext()) {
				String col_1=(String) it.next();
				if(list0.contains(col_1) !=true) {
					System.out.println("basecode ="+col_1);
					System.out.println("parentcode ="+list0.get(i));
				}else {
					String sql3=
							"select col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9='"+list0.get(3) +"'";
					java.util.List<String> list3=getList(sql3);
					System.out.println("list3的长度 = "+list3.size());
					  getFinalBase(list0,list3);//递归调用本函数，直至找到basecode
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("连接打开="+connection);
		init();
		System.out.println("连接打开="+connection);
		getBaseCode();
	}

 
}
