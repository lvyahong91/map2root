package com.lenovo.map2root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;





/**
 * 处理出映射表，表结构为t_map2root(string basecode,string rootcode)
 *
 */
public class App2 
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
	
	  public static void updateTable(String basecode,String rootcode) throws Exception {
		  String sql="insert into cdl_finance.t_map2root(memberid,parenth6) values (?,?)";
		  ps=connection.prepareStatement(sql);
		  ps.setString(1, basecode);
		  ps.setString(2, rootcode);
		  System.out.println("Running: " + sql);
		  ps.executeUpdate();
		  
	  }
	 
	
	/**
	 * 根据col_9为过滤条件获取col_1结果集
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getResult(String col_9) throws SQLException {
		//预防sql注入，使用预编译sql
		String sql="select  col_1 from cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9=?";
		ps=connection.prepareStatement(sql);
		ps.setString(1,col_9);
		System.out.println("Running: " + sql);
		ResultSet resultSet=null;
		resultSet=ps.executeQuery(sql);
		System.out.println("resultSet="+resultSet);
		List<String> list=new ArrayList<String>();
		while (resultSet.next()) {
			String col_1 = resultSet.getString(1);
			System.out.println("col1="+col_1);
			list.add(col_1);
		}
		return list;
		
	}
	/**
	 * 获取col_9的去重列表
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getDistinctResult() throws SQLException {
		//预防sql注入，使用预编译sql
		String sql="select  distinct col_9 from cdl_finance.bw_fin_zobpcm009_cdl_1";
		System.out.println("Running: " + sql);
		ResultSet resultSet1=null;
		System.out.println("预编译语句="+ps);
		resultSet1=ps.executeQuery(sql);
		System.out.println("resultSet1="+resultSet1);
		List<String> list=new ArrayList<String>();
		while (resultSet1.next()) {
			String col_9 = resultSet1.getString(1);
			System.out.println("col9="+col_9);
			list.add(col_9);
		}
		return list;
	}
	
	/**
	 * 将结果集转换为列表
	 * @param sql1
	 * @return
	 * @throws SQLException
	 */
	/*
	 * private static List<String> getList(List<String> list) throws SQLException {
	 * ResultSet resultSet3=null; java.util.List<String> list=new
	 * ArrayList<String>(); resultSet3=getResult(list); while (resultSet3.next()) {
	 * String col_9 = resultSet3.getString(1); System.out.println("col9="+col_9);
	 * list.add(col_9); } return list; }
	 */
	/**
	 * 查找rootcode的逻辑为根据parent列的code,过滤col_1的memberid,以此为第一重过滤
	 * 第二重，遍历第一重的memberid，若是在对应的parent列查找不到相同信息，该memberid就是basecode
	 * 若是在parent列能查找到相同的memberid,则需要以此memberid为parent的过滤条件，将过滤后的memberid列
	 * 重新作为第二重的条件，如此递归
	 * @throws Exception 
	 */
	public static void getBaseCode() throws Exception {
		//获得col_9的结果集，将结果集转换为list
		List<String > list_col_9=getDistinctResult();
		System.out.println("list_col_9的长度 = "+list_col_9.size());
		//根据list0的col_9列去过滤获取col_1的数据,将结果转换为list
		for (int i = 0; i < list_col_9.size(); i++) {
			List<String> list_col_1=getResult(list_col_9.get(i));
			System.out.println("list_col_1的长度 = "+list_col_1.size());
			getFinalBase(list_col_9.get(i),list_col_9,list_col_1);
		}
		
	}
	/**
	 * 采用递归函数，循环调用本函数找出rootcode
	 * @param list0
	 * @param list1
	 * @throws Exception 
	 */
	public static void getFinalBase(String col_9,List<String> list_col_9,List<String> list_col_1) throws Exception {
		for(int i=0;i<list_col_1.size();i++) {
			//第二重过滤，是根据list1的memberid作为条件，取得新的memberid列
			/*
			 * String sql2=
			 * "select  col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9='"
			 * +list_col_1.get(i) +"'"; java.util.List<String> list2=getList(sql2);
			 */
			List<String> list2=getResult(list_col_1.get(i));
			System.out.println("list2的长度 = "+list2.size());
			if (list2.size() ==0) {
				System.out.println("不需要再往下找");
				System.out.println("basecode ="+list_col_1.get(i));
				System.out.println("parentcode ="+col_9);
				updateTable(list_col_1.get(i), col_9);
				continue;
			}
			//将list2的数据和list0的值进行比对，若没有相同的，则list2的数据就是basecode
			//若有相同的数据，需要将该数据作为从list0的过滤条件，重新得到新的list2
			ListIterator<String> it=list2.listIterator();
			while(it.hasNext()) {
				String col_1=(String) it.next();
				if(list_col_9.contains(col_1) !=true) {
					System.out.println("basecode ="+col_1);
					System.out.println("parentcode ="+col_9);
					updateTable(col_1, col_9);
				}else {
					/*
					 * String sql3=
					 * "select col_1 from  cdl_finance.bw_fin_zobpcm009_cdl_1 where col_9='"+list0.
					 * get(3) +"'"; java.util.List<String> list3=getList(sql3);
					 */
					List<String> list3=getResult(col_9);
					System.out.println("list3的长度 = "+list3.size());
					  getFinalBase(col_9,list_col_9,list3);//递归调用本函数，直至找到basecode
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
