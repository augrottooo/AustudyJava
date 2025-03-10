package DoubleColorBall;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LotteryMain {
    public static void main(String[] args) {
        LotteryNumberGenerator generator = new LotteryNumberGenerator();
        List<Integer> winRedNumbers = generator.generateRedNumbers();
        int winBlueNumber = generator.generateBlueNumber();

        PasswordEncryptor encryptor = new PasswordEncryptor();
        AdminFunction adminFunction = new AdminFunction(encryptor, "admin123", winRedNumbers, winBlueNumber);

        Scanner scanner = new Scanner(System.in);
        while (true) {//写个死循环让程序一直跑
            System.out.println("请选择操作：1. 普通用户抽奖 2. 管理员登录 3. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除缓冲区

            if (choice == 1) {
                List<Integer> userRedNumbers = null;
                while (userRedNumbers == null) {
                    System.out.println("请输入6个红球号码（以空格分隔）：");
                    String[] redInput = scanner.nextLine().split(" ");
                    userRedNumbers = new ArrayList<>();
                    boolean isValid = true;
                    for (String num : redInput) {
                        try {
                            int number = Integer.parseInt(num);
                            if (number < 1 || number > 33) {
                                System.out.println("红球号码必须在1到33之间，请重新输入。");
                                isValid = false;
                                break;
                            }
                            userRedNumbers.add(number);
                        } catch (NumberFormatException e) {
                            System.out.println("输入的红球号码格式不正确，请重新输入。");
                            isValid = false;
                            break;
                        }
                    }
                    if (userRedNumbers.size() != 6) {
                        System.out.println("必须输入6个红球号码，请重新输入。");
                        isValid = false;
                    }
                    if (!isValid) {
                        userRedNumbers = null; // 重置，以便重新输入
                    }
                }

                int userBlueNumber = 0;
                boolean blueValid = false;
                while (!blueValid) {
                    System.out.println("请输入蓝球号码：");
                    userBlueNumber = scanner.nextInt();
                    if (userBlueNumber < 1 || userBlueNumber > 16) {
                        System.out.println("蓝球号码必须在1到16之间，请重新输入。");
                    } else {
                        blueValid = true;
                    }
                    System.out.println("输入的蓝球号码格式不正确，请重新输入。");
                    scanner.nextLine(); // 清除无效输入
                }

                LotteryResultChecker checker = new LotteryResultChecker();
                //int result = checker.checkLotteryResult(winRedNumbers, winBlueNumber, userRedNumbers, userBlueNumber);
                int result = checker.checkLotteryResult(adminFunction.getWinRedNumbers(), adminFunction.getWinBlueNumber(), userRedNumbers, userBlueNumber);
                if (result == 0) {
                    System.out.println("很遗憾，未中奖");
                } else {
                    System.out.println("恭喜您，中了" + result + "等奖");
                }
            } else if (choice == 2) {
                System.out.println("请输入密码：");
                String inputPassword = scanner.nextLine();
                if (adminFunction.login(inputPassword)) {
                    System.out.println("登录成功，欢迎管理员");
                    adminFunction.changeWinningNumbers();
                } else {
                    System.out.println("密码错误，登录失败");
                }
            } else if (choice == 3) {
                System.out.println("程序结束");
                return;
            }else {
                System.out.println("无效的选择");
            }
        }
    }
}