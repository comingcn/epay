/**   
 * @Title: DBUtils.java 
 * @Package: com.epay.xj.utils 
 * @author LZG, liuzhongguochn@gmail.com  
 * Copyright (c) 2018 北京中数合一科技有限公司
 */
package com.epay.xj.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Description:
 * @author LZG
 * @date 2018年07月02日
 */
public class DBUtils {

    // 数据库连接地址
    public static String URL;

    // 用户名
    public static String USERNAME;

    // 密码
    public static String PASSWORD;

    // mysql的驱动类
    public static String DRIVER;

    private static Properties prop = PropertiesUtils.read("/db-config.properties");

    // 使用静态块加载驱动程序
    static {
        URL = prop.getProperty("jdbc.url");
        USERNAME = prop.getProperty("jdbc.username");
        PASSWORD = prop.getProperty("jdbc.password");
        DRIVER = prop.getProperty("jdbc.driver");

        // System.out.println(URL + ":" + USERNAME + ":" + PASSWORD + ":" +
        // DRIVER);

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 定义一个获取数据库连接的方法
     * @return
     * @author LZG
     * @date 2018年07月02日
     */
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

    /**
     * @Description: 关闭数据库连接
     * @param rs
     * @param stat
     * @param conn
     * @author LZG
     * @date 2018年07月02日
     */
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

}
