package DoubleColorBall;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {
    //密码加密解密类
    /**
     * 使用SHA-256算法对密码进行加密
     * @param password 原始密码
     * @return 加密后的密码
     */
    public String encryptPassword(String password) {
        try {
            // 获取SHA - 256算法的消息摘要实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // 对原始密码进行哈希计算，得到字节数组
            byte[] hash = digest.digest(password.getBytes());
            // 用于存储加密后的十六进制字符串
            StringBuilder hexString = new StringBuilder();
            // 将字节数组转换为十六进制字符串
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 如果指定的算法不可用，抛出运行时异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 简单示例，这里假设解密后的密码与加密前相同（实际SHA-256不可逆）
     * @param encryptedPassword 加密后的密码
     * @return 解密后的密码（示例返回加密前密码）
     */
    public String decryptPassword(String encryptedPassword) {
        return encryptedPassword;
    }
}