package DoubleColorBall;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminFunction {
    //管理员功能类
    private PasswordEncryptor encryptor;// 密码加密解密工具类实例
    private String adminPassword;// 加密后的管理员密码
    private List<Integer> winRedNumbers;// 中奖的红球号码列表
    private int winBlueNumber;// 中奖的蓝球号码

    public AdminFunction(PasswordEncryptor encryptor, String adminPassword,
                         List<Integer> winRedNumbers, int winBlueNumber) {
        this.encryptor = encryptor;
        // 对原始管理员密码进行加密存储
        this.adminPassword = encryptor.encryptPassword(adminPassword);
        this.winRedNumbers = winRedNumbers;
        this.winBlueNumber = winBlueNumber;
    }

    // 管理员登录验证，inputPassword 输入的密码，return 是否登录成功
    public boolean login(String inputPassword) {
        // 对输入的密码进行加密
        String encryptedInput = encryptor.encryptPassword(inputPassword);
        // 比较加密后的输入密码与存储的加密管理员密码
        return encryptedInput.equals(adminPassword);
    }

    //管理员更改中奖号码
    public void changeWinningNumbers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新的6个红球号码（以空格分隔）：");
        String[] redInput = scanner.nextLine().split(" ");
        List<Integer> newRedNumbers = new ArrayList<>();
        for (String num : redInput) {
            try {
                // 将字符串转换为整数
                int number = Integer.parseInt(num);
                if (number < 1 || number > 33) {
                    System.out.println("红球号码必须在1到33之间");
                    return;
                }
                newRedNumbers.add(number);
            } catch (NumberFormatException e) {
                // 如果输入的字符串无法转换为整数，提示输入格式不正确
                System.out.println("输入的红球号码格式不正确");
                return;
            }
        }
        // 检查输入的红球号码数量是否为6个
        if (newRedNumbers.size()!= 6) {
            System.out.println("必须输入6个红球号码");
            return;
        }

        System.out.println("请输入新的蓝球号码：");
        try {
            int newBlueNumber = scanner.nextInt();
            if (newBlueNumber < 1 || newBlueNumber > 16) {
                System.out.println("蓝球号码必须在1到16之间");
                return;
            }
            winRedNumbers = newRedNumbers;
            winBlueNumber = newBlueNumber;
            System.out.println("中奖号码已成功更改");
        } catch (Exception e) {
            System.out.println("输入的蓝球号码格式不正确");
        }
    }
    public List<Integer> getWinRedNumbers() {
        return winRedNumbers;
    }

    public int getWinBlueNumber() {
        return winBlueNumber;
    }
}
