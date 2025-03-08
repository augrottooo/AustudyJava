package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 数据库连接工具类，用于获取与 MySQL 数据库的连接
public class MysqlConnection {
    // 数据库连接 URL，指定数据库的地址、端口和名称。
    private static final String URL = "jdbc:mysql://localhost:3306/atm_system";
    // 数据库用户名
    private static final String USER = "root";
    // 数据库密码
    private static final String PASSWORD = "Fkas514*mysql";

    // 获取数据库连接的静态方法
    public static Connection getConnection() throws SQLException {
        try {
            // 显式加载 MySQL JDBC 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 使用 DriverManager 获取数据库连接
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}