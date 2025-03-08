package main;

import java.math.BigDecimal;

// 用户信息实体类，用于封装用户的基本信息
public class User {
    // 用户唯一标识
    private int id;
    // 用户姓名
    private String name;
    // 用户手机号码
    private String phoneNumber;
    // 用户身份证号码
    private String idCardNumber;
    // 用户银行卡号码
    private String bankCardNumber;
    // 用户账户余额
    private BigDecimal balance;
    // 用户登录密码
    private String password;

    // 无参构造函数
    public User() {}

    // 有参构造函数，用于初始化用户信息
    public User(int id, String name, String phoneNumber, String idCardNumber, String bankCardNumber, BigDecimal balance, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.idCardNumber = idCardNumber;
        this.bankCardNumber = bankCardNumber;
        this.balance = balance;
        this.password = password;
    }

    // 获取用户 ID 的方法
    public int getId() {
        return id;
    }

    // 设置用户 ID 的方法
    public void setId(int id) {
        this.id = id;
    }

    // 获取用户姓名的方法
    public String getName() {
        return name;
    }

    // 设置用户姓名的方法
    public void setName(String name) {
        this.name = name;
    }

    // 获取用户手机号码的方法
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // 设置用户手机号码的方法
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // 获取用户身份证号码的方法
    public String getIdCardNumber() {
        return idCardNumber;
    }

    // 设置用户身份证号码的方法
    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    // 获取用户银行卡号码的方法
    public String getBankCardNumber() {
        return bankCardNumber;
    }

    // 设置用户银行卡号码的方法
    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    // 获取用户账户余额的方法
    public BigDecimal getBalance() {
        return balance;
    }

    // 设置用户账户余额的方法
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // 获取用户登录密码的方法
    public String getPassword() {
        return password;
    }

    // 设置用户登录密码的方法
    public void setPassword(String password) {
        this.password = password;
    }
}