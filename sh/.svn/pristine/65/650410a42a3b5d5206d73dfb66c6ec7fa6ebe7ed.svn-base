/*******************************************************************
 * $Header$
 * $Revision: 1.0$
 * $Date: Jun 12, 2018$
 * Copyright (c) 2017-2018 北京中数合一科技有限公司.
 * All rights reserved. 
 *******************************************************************/
package com.zshy.datacleaning.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * 数据库操作工具类
 * 
 * @author lamp
 * 
 */
public class DBUtil {

	// 数据库连接地址
	public static String URL;
	// 用户名
	public static String USERNAME;
	// 密码
	public static String PASSWORD;
	// mysql的驱动类
	public static String DRIVER;

	private static Properties prop = PropertiesUtil.read("/config/db-config.properties");

	// 使用静态块加载驱动程序
	static {
		URL = prop.getProperty("jdbc.url");
		USERNAME = prop.getProperty("jdbc.username");
		PASSWORD = prop.getProperty("jdbc.password");
		DRIVER = prop.getProperty("jdbc.driver");
		
		//System.out.println(URL + ":" + USERNAME + ":" + PASSWORD + ":" + DRIVER);
		
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 定义一个获取数据库连接的方法
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("获取连接失败");
		}
		return conn;
	}

	// 关闭数据库连接
	public static void close(ResultSet rs, Statement stat, Connection conn) {
		try {
			if (rs != null)
				rs.close();
			if (stat != null)
				stat.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能说明：检查数据库某个表是否存在某个列
	 * 
	 * @param tableName 表名
	 * @param columnName 列名
	 * @return boolean
	 */
	public static boolean columnContains(String tableName, String columnName) {
		Statement stat = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		Connection conn = getConnection();

		// 数据库列字段的List集合
		List<String> tableColumnList = new ArrayList<String>();

		try {
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from " + tableName);
			rsmd = rs.getMetaData();

			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				tableColumnList.add(rsmd.getColumnName(i));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		close(rs, stat, conn);

		if (!tableColumnList.contains(columnName)) 
			return false;
		return true;
	}

}
