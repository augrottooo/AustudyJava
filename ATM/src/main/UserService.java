package main;

import java.math.BigDecimal;// 用于精确的十进制运算，避免浮点数运算带来的精度问题。
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;// 用于创建正则表达式模式，以验证输入的格式。

// 用户服务类，提供用户注册、登录、存款、取款、转账等功能的业务逻辑
public class UserService {
    // 手机号码格式验证正则表达式，要求为 11 位数字
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[0-9]{11}$");
    // 身份证号码格式验证正则表达式，要求为 18 位数字，最后一位可以是数字或 X/x
    private static final Pattern ID_CARD_NUMBER_PATTERN = Pattern.compile("^[0-9]{17}[0-9Xx]$");
    // 银行卡号码格式验证正则表达式，要求为 16 到 19 位数字
    private static final Pattern BANK_CARD_NUMBER_PATTERN = Pattern.compile("^[0-9]{16,19}$");
    // 密码格式验证正则表达式，要求为 6 位数字
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9]{6}$");

    // 用户注册方法
    public boolean register(String name, String phoneNumber, String idCardNumber, String bankCardNumber, String password) {
        // 验证输入的手机号码、身份证号码、银行卡号码和密码是否符合格式要求
        if (!isValidPhoneNumber(phoneNumber) || !isValidIdCardNumber(idCardNumber) ||
                !isValidBankCardNumber(bankCardNumber) || !isValidPassword(password)) {
            return false;
        }
        // 插入用户信息的 SQL 语句
        String sql = "INSERT INTO users (name, phone_number, id_card_number, bank_card_number, balance, password) VALUES (?,?,?,?,?,?)";
        // 使用 try-with-resources 语句创建数据库连接和预编译的 SQL 语句对象，确保资源在使用后自动关闭。
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {//在 try 关键字后面的括号中声明需要管理的资源
            // 设置 SQL 语句中的参数
            pstmt.setString(1, name);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, idCardNumber);
            pstmt.setString(4, bankCardNumber);
            // 初始余额为 0
            pstmt.setBigDecimal(5, BigDecimal.ZERO);
            pstmt.setString(6, password);
            // 执行 SQL 语句并返回受影响的行数
            int rows = pstmt.executeUpdate();
            // 如果受影响的行数大于 0，表示注册成功
            return rows > 0;
        } catch (SQLException e) {
            // 捕获可能出现的 SQL 异常，打印异常信息并返回 false
            e.printStackTrace();
            return false;
        }
    }

    // 用户登录方法
    public User login(String idCardNumber, String password) {
        // 查询用户信息的 SQL 语句，从 users 表中查询与输入的身份证号码和密码匹配的用户信息。
        String sql = "SELECT * FROM users WHERE id_card_number = ? AND password = ?";
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置 SQL 语句中的参数
            pstmt.setString(1, idCardNumber);
            pstmt.setString(2, password);
            // 执行查询并获取结果集
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {// 判断结果集中是否有下一行记录，如果有，则表示登录成功
                // 从结果集中获取用户信息
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String bankCardNumber = rs.getString("bank_card_number");
                BigDecimal balance = rs.getBigDecimal("balance");
                // 创建用户对象并返回
                return new User(id, name, phoneNumber, idCardNumber, bankCardNumber, balance, password);
            }
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
        return null;
    }

    // 存款方法
    public boolean deposit(int userId, BigDecimal amount, String sourceBankCardNumber) {
        // 验证存款金额是否为负数
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        // 更新用户余额的 SQL 语句
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置 SQL 语句中的参数
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, userId);
            // 执行 SQL 语句并返回受影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                // 记录存款交易信息
                recordTransaction(userId, "Deposit", amount, sourceBankCardNumber);
                return true;
            }
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
        return false;
    }

    // 取款方法
    public boolean withdraw(int userId, BigDecimal amount, String sourceBankCardNumber) {
        // 获取用户信息
        User user = getUserById(userId);
        // 验证用户是否存在、取款金额是否为负数以及是否超过用户余额
        if (user == null || amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(user.getBalance()) > 0) {
            return false;
        }
        // 更新用户余额的 SQL 语句
        String sql = "UPDATE users SET balance = balance - ? WHERE id = ?";
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置 SQL 语句中的参数
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, userId);
            // 执行 SQL 语句并返回受影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                // 记录取款交易信息
                recordTransaction(userId, "Withdraw", amount, sourceBankCardNumber);
                return true;
            }
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
        return false;
    }

    // 转账方法
    public boolean transfer(int userId, String targetBankCardNumber, BigDecimal amount) {
        // 获取转账用户信息
        User user = getUserById(userId);
        // 获取目标用户信息
        User targetUser = getUserByBankCardNumber(targetBankCardNumber);
        // 验证转账用户、目标用户是否存在，转账金额是否为负数以及是否超过转账用户余额
        if (user == null || targetUser == null || amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(user.getBalance()) > 0) {
            return false;
        }
        try (Connection conn = MysqlConnection.getConnection()) {
            conn.setAutoCommit(false);// 手动开启事务
            try {
                // 先更新转账用户的余额
                String sql1 = "UPDATE users SET balance = balance - ? WHERE id = ?";
                try (PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {
                    pstmt1.setBigDecimal(1, amount);
                    pstmt1.setInt(2, userId);
                    pstmt1.executeUpdate();
                }
                // 再更新目标用户的余额
                String sql2 = "UPDATE users SET balance = balance + ? WHERE bank_card_number = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                    pstmt2.setBigDecimal(1, amount);
                    pstmt2.setString(2, targetBankCardNumber);
                    pstmt2.executeUpdate();
                }
                // 记录转账交易信息
                recordTransaction(userId, "Transfer", amount, targetBankCardNumber);
                recordTransaction(targetUser.getId(), "Receive", amount, user.getBankCardNumber());
                conn.commit();// 提交事务
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
        return false;
    }

    // 根据用户 ID 获取用户信息的方法
    private User getUserById(int userId) {
        // 查询用户信息的 SQL 语句
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置 SQL 语句中的参数
            pstmt.setInt(1, userId);
            // 执行查询并获取结果集
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 从结果集中获取用户信息
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String idCardNumber = rs.getString("id_card_number");
                String bankCardNumber = rs.getString("bank_card_number");
                BigDecimal balance = rs.getBigDecimal("balance");
                String password = rs.getString("password");
                // 创建用户对象并返回
                return new User(id, name, phoneNumber, idCardNumber, bankCardNumber, balance, password);
            }
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
        return null;
    }

    // 根据银行卡号码获取用户信息的方法
    private User getUserByBankCardNumber(String bankCardNumber) {
        // 查询用户信息的 SQL 语句
        String sql = "SELECT * FROM users WHERE bank_card_number = ?";
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置 SQL 语句中的参数
            pstmt.setString(1, bankCardNumber);
            // 执行查询并获取结果集
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 从结果集中获取用户信息
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String idCardNumber = rs.getString("id_card_number");
                BigDecimal balance = rs.getBigDecimal("balance");
                String password = rs.getString("password");
                // 创建用户对象并返回
                return new User(id, name, phoneNumber, idCardNumber, bankCardNumber, balance, password);
            }
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
        return null;
    }

    // 记录交易信息的方法
    private void recordTransaction(int userId, String transactionType, BigDecimal amount, String targetBankCardNumber) {
        // 插入交易记录的 SQL 语句
        String sql = "INSERT INTO transactions (user_id, transaction_type, amount, target_bank_card_number) VALUES (?,?,?,?)";
        try (Connection conn = MysqlConnection.getConnection();
             // 创建预编译的 SQL 语句对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 设置 SQL 语句中的参数
            pstmt.setInt(1, userId);
            pstmt.setString(2, transactionType);
            pstmt.setBigDecimal(3, amount);
            pstmt.setString(4, targetBankCardNumber);
            // 执行 SQL 语句
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // 打印异常信息
            e.printStackTrace();
        }
    }

    // 验证手机号码格式的方法
    private boolean isValidPhoneNumber(String phoneNumber) {
        // 使用正则表达式验证手机号码格式
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    // 验证身份证号码格式的方法
    private boolean isValidIdCardNumber(String idCardNumber) {
        // 使用正则表达式验证身份证号码格式
        return ID_CARD_NUMBER_PATTERN.matcher(idCardNumber).matches();
    }

    // 验证银行卡号码格式的方法
    private boolean isValidBankCardNumber(String bankCardNumber) {
        // 使用正则表达式验证银行卡号码格式
        return BANK_CARD_NUMBER_PATTERN.matcher(bankCardNumber).matches();
    }

    // 验证密码格式的方法
    private boolean isValidPassword(String password) {
        // 使用正则表达式验证密码格式
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}