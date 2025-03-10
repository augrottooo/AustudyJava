package main;

import java.io.IOException;// 用于处理输入输出操作中可能出现的异常
import java.io.PrintWriter;// 用于将文本内容写入文件
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;// 用于使用 Java Stream API 进行数据处理

// 交易记录服务类，提供打印交易历史记录并导出为 CSV 文件的功能
public class TransactionService {
    // 打印交易历史记录并导出为 CSV 文件的方法
    public void printTransactionHistory(int userId, String filePath) {
        // 查询交易记录的 SQL 语句
        String sql = "SELECT transaction_type, amount, target_bank_card_number, transaction_time FROM transactions WHERE user_id = ?";
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置 SQL 语句中的参数
            pstmt.setInt(1, userId);
            // 执行查询并获取结果集
            ResultSet rs = pstmt.executeQuery();
            try (PrintWriter writer = new PrintWriter(filePath)) {
                // 写入 CSV 文件的表头
                writer.println("Transaction Type,Amount,Target Bank Card Number,Transaction Time");
                while (rs.next()) {
                    // 从结果集中获取交易信息
                    String transactionType = rs.getString("transaction_type");
                    double amount = rs.getDouble("amount");
                    String targetBankCardNumber = rs.getString("target_bank_card_number");
                    String transactionTime = rs.getString("transaction_time");
                    // 使用 Java Stream API 将交易信息拼接成一行 CSV 数据
                    String line = Stream.of(transactionType, String.valueOf(amount), targetBankCardNumber, transactionTime)
                            .map(field -> field == null ? "" : field.replaceAll(",", ""))
                            .reduce((a, b) -> a + "," + b)
                            .orElse("");
                    // 写入一行 CSV 数据
                    writer.println(line);
                }
            } catch (IOException e) {
                // 打印异常信息
                e.printStackTrace();
            }
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
    }
}