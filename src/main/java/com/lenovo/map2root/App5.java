package com.lenovo.map2root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 处理出映射表，表结构为t_map2root(string basecode,string rootcode)
 *
 */
public class App5 {
	private static String driverName = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/cdl_finance?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8";
	private static String user = "root";
	private static String password = "root";

	private static Connection connection = null;
	private static PreparedStatement ps = null;
	private static ResultSet resultSet = null;

	private static List<String> list0 = new ArrayList<String>();
	private static Map<String, String> map = new HashMap<String, String>();
	private static Map<String, List<String>>resultMap = new HashMap<String, List<String>>();

	/**
	 * 创建hive驱动
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 创建映射表
	 * 
	 * @throws Exception
	 */

	public static void updateTable(String basecode, String rootcode) throws Exception {
		String sql = "insert into cdl_finance.t_map2root_dup(memberid,parenth9) values (?,?)";
		ps = connection.prepareStatement(sql);

		ps.setString(1, basecode);
		ps.setString(2, rootcode);
		System.out.println("Running: " + sql);
		ps.executeUpdate();

	}

	/**
	 * 根据sql获取结果集
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getResult(String sql) throws SQLException {
		// 预防sql注入，使用预编译sql
		ps = connection.prepareStatement(sql);
		System.out.println("Running: " + sql);
		resultSet = ps.executeQuery(sql);
		System.out.println("resultSet=" + resultSet);
//		resultSet.last();
//		System.out.println("行数="+resultSet.getRow());
		return resultSet;
	}

	/**
	 * 将结果集转换为列表
	 * 
	 * @param sql1
	 * @throws SQLException
	 */
	private static void getList(String sql1) throws SQLException {
		resultSet = getResult(sql1);
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCount = rsmd.getColumnCount();
		System.out.println("列数=" + columnCount);
		while (resultSet.next()) {
			if (columnCount == 1) {
				String col_11 = resultSet.getString(1);
				if (col_11 != null) {
					list0.add(col_11);

				}
			} else {
				String col_1 = resultSet.getString(1);
				String col_11 = resultSet.getString(2);
				map.put(col_1, col_11);
			}
		}

	}

	/**
	 * 查找rootcode的逻辑为根据parent列的code,过滤col_1的memberid,以此为第一重过滤
	 * 第二重，遍历第一重的memberid，若是在对应的parent列查找不到相同信息，该memberid就是basecode
	 * 若是在parent列能查找到相同的memberid,则需要以此memberid为parent的过滤条件，将过滤后的memberid列
	 * 重新作为第二重的条件，如此递归
	 * 
	 * @throws Exception
	 */
	public static void getBaseCode() throws Exception {
		// 获得col_9的结果集，将结果集转换为list
		String sql1 = "select distinct col_11 from cdl_finance.bw_fin_zobpcm009_cdl_1 where col_11 != ''";
		getList(sql1);
		System.out.println("list1的长度 = " + list0.size());
		String sql2 = "select col_1,col_11 from cdl_finance.bw_fin_zobpcm009_cdl_1 where col_11 !=''";
		getList(sql2);
		for (String col_11 : list0) {
			getFinalBase(list0, col_11, col_11, map);

		}

	

		// 根据list1的数据去表里获取col_1的数据,将结果转换为list

		updateTable(resultMap);

	}

	private static void updateTable(Map<String, List<String>> resultMap2) {
		String sql = "insert into cdl_finance.t_map2root_dup(memberid,parenth9) values (?,?)";
		try {
			ps = connection.prepareStatement(sql);
			for (Entry<String, List<String>> entry : resultMap2.entrySet()) {
				
				for (String basecode : entry.getValue()) {
					ps.setString(1, basecode);
					ps.setString(2, entry.getKey());
					System.out.println("Running: " + sql);
					ps.executeUpdate();
					
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 采用递归函数，循环调用本函数找出rootcode
	 * 
	 * @param list0
	 * @param list1
	 * @throws Exception
	 */
	public static void getFinalBase(List<String> list0, String col_11, String col_11_dup, Map<String, String> map)
			throws Exception {

		List<String> keyList = new ArrayList<String>();
		for (String Key : map.keySet()) {
			// key以col_11为值
			if (map.get(Key).equals(col_11)) {
				keyList.add(Key);
			}
		}
		for (String key : keyList) {
			if (!list0.contains(key)) {
//				resultMap.put(key, map.get(col_11_dup));  //此处key有一定概率被覆盖，出现数据丢失
				if (resultMap.containsKey(col_11_dup)) {
					resultMap.get(col_11_dup).add(key);
				}else {
					List<String> baseCodeList = new ArrayList<String>();
					baseCodeList.add(key);
					resultMap.put(col_11_dup, baseCodeList);
				}
			} else {
				getFinalBase(list0, key, col_11_dup, map);

			}

		}

	}

	public static void main(String[] args) throws Exception {
		System.out.println("连接打开=" + connection);
		init();
		System.out.println("连接打开=" + connection);
		getBaseCode();

	}
}
