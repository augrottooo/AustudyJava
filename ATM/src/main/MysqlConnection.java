package main;

import java.sql.Connection;// 是 Java 中使用 JDBC 进行数据库操作的基础，通过它可以执行 SQL 语句、管理事务等。
import java.sql.DriverManager;// 负责管理 JDBC 驱动程序的加载，并为应用程序提供与数据库建立连接的方法
import java.sql.SQLException;// SQLException 是一个异常类，当在执行 JDBC 操作过程中出现 SQL 相关的错误时，会抛出该异常，用于捕获和处理数据库操作中可能出现的异常情况

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
        /*
        try {
            // 显式加载 MySQL JDBC 驱动（注册驱动）
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        // 使用 DriverManager 获取数据库连接，如果连接成功，会返回一个 Connection 对象；如果连接失败，会抛出 SQLException 异常
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}