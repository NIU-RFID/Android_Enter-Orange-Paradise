package com.example.niubig;

import android.os.Handler;
import android.widget.TextView;

public class LotteryUIHandler {

    private TextView resultTextView;
    private Handler handler;
    private int changeInterval = 100;  // 每次跳動顯示的間隔時間（毫秒）
    private int totalDuration = 3000;  // 跳動的總時間（毫秒）
    private String finalPrize = "";  // 最終結果

    public LotteryUIHandler(TextView resultTextView) {
        this.resultTextView = resultTextView;
        this.handler = new Handler();
    }

    /**
     * 開始跳動顯示抽獎結果
     */
    public void startLottery(LotteryManager lotteryManager) {
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + totalDuration;

        // 設置定時器，讓文字跳動
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() < endTime) {
                    // 隨機選擇一個獎品來顯示
                    String tempPrize = lotteryManager.getRandomPrize();
                    resultTextView.setText(tempPrize);

                    // 每隔一段時間更新一次顯示
                    handler.postDelayed(this, changeInterval);
                } else {
                    // 結束跳動，顯示最終結果
                    resultTextView.setText(finalPrize);
                }
            }
        });

        // 在最後的結果確定後，顯示最終獎品
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalPrize = lotteryManager.drawPrize();  // 確定最終中獎結果
            }
        }, totalDuration);
    }

    /**
     * 返回最後的中獎結果
     */
    public String getFinalPrize() {
        return finalPrize;
    }
}

