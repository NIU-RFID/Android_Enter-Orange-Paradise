package com.example.niubig;

import java.util.List;
import java.util.Random;

public class LotteryManager {

    private List<Prize> prizeList;  // 獎品清單

    public LotteryManager(List<Prize> prizeList) {
        this.prizeList = prizeList;
    }

    /**
     * 根據中獎機率來決定最終的獎品
     */
    public String drawPrize() {
        // 計算總機率
        double totalProbability = 0.0;
        for (Prize prize : prizeList) {
            totalProbability += prize.getProbability();
        }

        // 生成一個隨機數，並根據這個數來抽取獎品
        Random random = new Random();
        double randomValue = random.nextDouble() * totalProbability;

        double cumulativeProbability = 0.0;
        for (Prize prize : prizeList) {
            cumulativeProbability += prize.getProbability();
            if (randomValue <= cumulativeProbability) {
                return prize.getName();  // 返回中獎的獎品名稱
            }
        }

        return "未中獎";  // 如果沒有中獎，返回未中獎
    }

    /**
     * 隨機選擇一個獎品名稱，用來顯示跳動的獎品
     */
    public String getRandomPrize() {
        Random random = new Random();
        int index = random.nextInt(prizeList.size());
        return prizeList.get(index).getName();
    }
}
