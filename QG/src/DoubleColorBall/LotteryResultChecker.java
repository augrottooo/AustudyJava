package DoubleColorBall;
import java.util.List;

public class LotteryResultChecker {
    //中奖判断类
    /**
     * 判断用户输入的号码是否中奖
     * winRedNumbers 中奖的红球号码列表
     * winBlueNumber 中奖的蓝球号码
     * userRedNumbers 用户输入的红球号码列表
     * userBlueNumber 用户输入的蓝球号码
     * return 中奖等级，0表示未中奖
     */
    public int checkLotteryResult(List<Integer> winRedNumbers, int winBlueNumber,
                                  List<Integer> userRedNumbers, int userBlueNumber) {
        // 记录用户输入的红球号码与中奖红球号码匹配的个数
        int redMatchCount = 0;
        // 遍历检查用户输入的红球号码是否在中奖红球号码列表中
        for (int number : userRedNumbers) {
            if (winRedNumbers.contains(number)) {
                redMatchCount++;
            }
        }
        // 判断用户输入的蓝球号码是否与中奖蓝球号码匹配
        boolean blueMatch = userBlueNumber == winBlueNumber;

        // 根据红球和蓝球的匹配情况确定中奖等级
        if (redMatchCount == 6 && blueMatch) {
            return 1; // 一等奖
        } else if (redMatchCount == 6 &&!blueMatch) {
            return 2; // 二等奖
        } else if (redMatchCount == 5 && blueMatch) {
            return 3; // 三等奖
        } else if (redMatchCount == 5 &&!blueMatch || redMatchCount == 4 && blueMatch) {
            return 4; // 四等奖
        } else if (redMatchCount == 4 &&!blueMatch || redMatchCount == 3 && blueMatch) {
            return 5; // 五等奖
        } else if (redMatchCount < 3 && blueMatch) {
            return 6; // 六等奖
        } else {
            return 0; // 未中奖
        }
    }
}
