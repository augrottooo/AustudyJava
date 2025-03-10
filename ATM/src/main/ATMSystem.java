package main;


import java.math.BigDecimal;
import java.util.Scanner;

// 主程序类，包含系统的入口方法和菜单界面
public class ATMSystem {
    // 最大登录尝试次数
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    // 当前登录的用户
    private static User currentUser;
    // 用户服务类实例
    private static UserService userService = new UserService();
    // 交易记录服务类实例
    private static TransactionService transactionService = new TransactionService();

    public static void main(String[] args) {
        // 创建 Scanner 对象用于读取用户输入
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // 显示主菜单选项
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 退出");
            System.out.print("请选择操作: ");
            // 读取用户选择的操作
            int choice = scanner.nextInt();
            // 消耗换行符
            scanner.nextLine();
            switch (choice) {
                case 1:
                    // 调用注册方法
                    register(scanner);
                    break;
                case 2:
                    if (login(scanner)) {
                        // 登录成功后进入用户操作菜单
                        showUserMenu(scanner);
                    }
                    break;
                case 3:
                    // 退出程序
                    System.out.println("感谢使用，再见！");
                    return;
                default:
                    // 输入无效选项时提示用户重新输入
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    // 用户注册方法
    private static void register(Scanner scanner) {
        System.out.print("请输入姓名: ");
        String name = scanner.nextLine();
        System.out.print("请输入手机号码: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("请输入身份证号码: ");
        String idCardNumber = scanner.nextLine();
        System.out.print("请输入银行卡号码: ");
        String bankCardNumber = scanner.nextLine();
        System.out.print("请输入密码（6 位数字）: ");
        String password = scanner.nextLine();
        // 调用用户服务类的注册方法
        if (userService.register(name, phoneNumber, idCardNumber, bankCardNumber, password)) {
            System.out.println("注册成功！");
        } else {
            System.out.println("注册失败，请检查输入信息。");
        }
    }

    // 用户登录方法
    private static boolean login(Scanner scanner) {
        int attempts = 0;
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.print("请输入身份证号码: ");
            String idCardNumber = scanner.nextLine();
            System.out.print("请输入密码: ");
            String password = scanner.nextLine();
            // 调用用户服务类的登录方法
            currentUser = userService.login(idCardNumber, password);
            if (currentUser != null) {
                System.out.println("登录成功！欢迎，" + currentUser.getName());
                return true;
            } else {
                attempts++;
                System.out.println("登录失败，你还有 " + (MAX_LOGIN_ATTEMPTS - attempts) + " 次尝试机会。");
            }
        }
        System.out.println("登录尝试次数过多，账户已锁定。");
        return false;
    }

    // 用户操作菜单方法
    private static void showUserMenu(Scanner scanner) {
        while (true) {
            // 显示用户操作菜单选项
            System.out.println("1. 存款");
            System.out.println("2. 取款");
            System.out.println("3. 转账");
            System.out.println("4. 打印流水账单");
            System.out.println("5. 退出登录");
            System.out.print("请选择操作: ");
            int choice = scanner.nextInt();
            // 消耗换行符
            scanner.nextLine();
            switch (choice) {
                case 1:
                    // 调用存款方法
                    deposit(scanner);
                    break;
                case 2:
                    // 调用取款方法
                    withdraw(scanner);
                    break;
                case 3:
                    // 调用转账方法
                    transfer(scanner);
                    break;
                case 4:
                    // 调用打印流水账单方法
                    printTransactionHistory(scanner);
                    break;
                case 5:
                    // 退出登录
                    System.out.println("已退出登录。");
                    currentUser = null;
                    return;
                default:
                    // 输入无效选项时提示用户重新输入
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    // 存款方法
    private static void deposit(Scanner scanner) {
        System.out.print("请输入存款金额: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            System.out.print("请输入存款银行卡号: ");
            String sourceBankCardNumber = scanner.nextLine();
            if (userService.deposit(currentUser.getId(), amount, sourceBankCardNumber)) {
                System.out.println("存款成功！当前余额: " + currentUser.getBalance().add(amount));
            } else {
                System.out.println("存款失败，请检查输入金额。");
            }
        } catch (NumberFormatException e) {
            System.out.println("输入的金额格式不正确，请输入有效的数字。");
        }
    }

    // 取款方法
    private static void withdraw(Scanner scanner) {
        System.out.print("请输入取款金额: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            System.out.print("请输入取款来源银行卡号: ");
            String sourceBankCardNumber = scanner.nextLine();
            // 调用用户服务类的取款方法
            if (userService.withdraw(currentUser.getId(), amount, sourceBankCardNumber)) {
                System.out.println("取款成功！当前余额: " + currentUser.getBalance().subtract(amount));
            } else {
                System.out.println("取款失败，请检查余额或输入金额。");
            }
        } catch (NumberFormatException e) {
            System.out.println("输入的金额格式不正确，请输入有效的数字。");
        }
    }

    // 转账方法
    private static void transfer(Scanner scanner) {
        System.out.print("请输入目标银行卡号码: ");
        String targetBankCardNumber = scanner.nextLine();
        System.out.print("请输入转账金额: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            // 调用用户服务类的转账方法
            if (userService.transfer(currentUser.getId(), targetBankCardNumber, amount)) {
                System.out.println("转账成功！当前余额: " + currentUser.getBalance().subtract(amount));
            } else {
                System.out.println("转账失败，请检查目标账户或输入金额。");
            }
        } catch (NumberFormatException e) {
            System.out.println("输入的金额格式不正确，请输入有效的数字。");
        }
    }

    // 打印流水账单方法
    private static void printTransactionHistory(Scanner scanner) {
        System.out.print("请输入要保存的文件路径（例如: /Users/hj/Desktop/transactions.csv）: ");
        String filePath = scanner.nextLine();
        // 调用交易记录服务类的打印交易历史记录方法
        transactionService.printTransactionHistory(currentUser.getId(), filePath);
        System.out.println("交易记录已导出到 " + filePath);
    }
}