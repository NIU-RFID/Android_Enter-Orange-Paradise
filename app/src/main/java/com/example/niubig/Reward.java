package com.example.niubig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import android.widget.Toast;

public class Reward extends AppCompatActivity {
    private TextView resultTextView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private LotteryManager lotteryManager;
    private LotteryUIHandler lotteryUIHandler;
    boolean draw_done;
    String draw_reward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reward);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init_data();
    }
    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void init_data(){
        sharedPreferences = getSharedPreferences("isDraw", MODE_PRIVATE);
        draw_done = sharedPreferences.getBoolean("done", false);
        draw_reward = sharedPreferences.getString("reward", "無");
        editor = sharedPreferences.edit();
        resultTextView = findViewById(R.id.resultTextView);
        // 初始化獎品清單
        List<Prize> prizeList = new ArrayList<>();
        prizeList.add(new Prize("50元折價卷", 0.2));  // 20%機率
        prizeList.add(new Prize("30元折價卷", 0.2));  // 20%機率
        prizeList.add(new Prize("果醬一份", 0.2));  // 20%機率
        prizeList.add(new Prize("米餅一份", 0.2));  // 20%機率
        prizeList.add(new Prize("金棗糖果", 0.2));  // 20%機率

        // 初始化 LotteryManager 和 LotteryUIHandler
        lotteryManager = new LotteryManager(prizeList);
        lotteryUIHandler = new LotteryUIHandler(resultTextView);
    }
    public void drawing(View view) {
        if (!draw_done){
            // 開始抽獎
            lotteryUIHandler.startLottery(lotteryManager);

            // 設置延遲，等待跳動結束後獲取最終獎品
            resultTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 獲取最終的獎品
                    String finalPrize = lotteryUIHandler.getFinalPrize();
                    // 顯示最終獎品
                    resultTextView.setText("最終獎品: " + finalPrize);
                    editor.putBoolean("done", true);
                    editor.putString("reward", finalPrize);
                    editor.apply();
                    draw_done = true;
                    draw_reward = finalPrize;
                }
            }, 3500);  // 設定延遲時間，讓它比跳動時間稍微長一點
        }else {
            Toast.makeText(this, "您已抽過獎品, 最終獎品為"+ draw_reward, Toast.LENGTH_SHORT).show();
        }
    }
}