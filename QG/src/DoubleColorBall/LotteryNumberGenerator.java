package DoubleColorBall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LotteryNumberGenerator {
    //双色球号码生成类

    //生成随机的红球号码，return 包含6个红球号码的列表
    public List<Integer> generateRedNumbers() {
        List<Integer> redNumbers = new ArrayList<>();
        Random random = new Random();//随机数生成器
        while (redNumbers.size() < 6) {
            int number = random.nextInt(33) + 1;
            if (!redNumbers.contains(number)) {// 检查生成的号码是否已经在列表中，如果不在则添加到列表
                redNumbers.add(number);
            }
        }
        // 对生成的红球号码列表进行升序排序
        Collections.sort(redNumbers);

        // 输出随机生成的红球号码，调试用。
         System.out.print("随机生成的红球号码为: ");
         for (int i = 0; i < redNumbers.size(); i++) {
         if (i > 0) {
         System.out.print(", ");
         }
         System.out.print(redNumbers.get(i));
         }
         System.out.println();


        return redNumbers;
    }

    //生成随机的蓝球号码，return 蓝球号码
    public int generateBlueNumber() {
        Random random = new Random();
         // 输出随机生成的蓝球号码，调试用。
        int a = random.nextInt(16) + 1;
         System.out.println("随机生成的蓝球号码为: "+a);

        return a;
    }
}