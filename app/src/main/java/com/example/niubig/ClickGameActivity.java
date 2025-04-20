package com.example.niubig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ClickGameActivity extends AppCompatActivity {
    ArrayList<ClickGameData> images_list;
    TextView problem_textView, score_textView;
    Integer now_scoce, target_sccre;
    String item_name;
    Random random;
    ConstraintLayout image_canvas;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_click_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        _initGame();
        getRandomItem();
        putAllImages();
    }
    public void _initGame()
    {
        problem_textView = findViewById(R.id.problem_textView);
        score_textView = findViewById(R.id.score_textView);
        score_textView.setText(String.valueOf(0));
        images_list = new ArrayList<>();
        images_list.add(new ClickGameData("炸彈","bomb", -50)); // 首位為炸彈，抽取題目時略過
        images_list.add(new ClickGameData("金棗","golden_dates", 10));
        images_list.add(new ClickGameData("帝王柑","orange", 20));
        images_list.add(new ClickGameData("洛神花","roselle", 30));
        images_list.add(new ClickGameData("木瓜","papaya", 40));
        images_list.add(new ClickGameData("水蜜桃","peach", 50));
        now_scoce=0;
        target_sccre=300;
        image_canvas = findViewById(R.id.image_canvas);
        sharedPreferences = getSharedPreferences("isDone", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void getRandomItem(){
        random = new Random(System.currentTimeMillis());
        if (images_list.size() > 1) {
            int randomIndex = random.nextInt(images_list.size() - 1) + 1; // 取 1 到 size-1 之間的索引
            item_name = String.valueOf(images_list.get(randomIndex).getName());
            problem_textView.setText(item_name);
        }
        else{
            problem_textView.setText("確保 images_list 長度大於 1");
        }
    }
    public void putAllImages(){
        // 新增 images_list 所有元素為 ImageButton, 圖片用 .getImagePath 取得路徑
        // 並且將所有按鈕加入到 image_canvas 以隨機的 (x,y) 座標出現, 但不能超出螢幕範圍
        image_canvas.removeAllViews(); // 清除
        // 取得畫布寬高（需等畫面 layout 完成後才準確）
        image_canvas.post(() -> {
            int canvasWidth = image_canvas.getWidth();
            int canvasHeight = image_canvas.getHeight();

            for (ClickGameData data : images_list) {
                ImageButton imgBtn = new ImageButton(this);

                // 設定圖片資源
                int resId = getResources().getIdentifier(data.getImagePath(), "drawable", getPackageName());
                imgBtn.setImageResource(resId);

                // 美化按鈕
                imgBtn.setBackground(null);
                imgBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
                int size = 200; // 圖片大小（寬高一樣）

                // 隨機位置，確保不會超出邊界
                int maxX = canvasWidth - size;
                int maxY = canvasHeight - size;
                int x = random.nextInt(Math.max(1, maxX));
                int y = random.nextInt(Math.max(1, maxY));

                // 設定 layout
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);
                imgBtn.setLayoutParams(params);
                imgBtn.setX(x);
                imgBtn.setY(y);

                imgBtn.setOnClickListener(v -> {
                    if (data.getName().equals(item_name)) {
                        now_scoce += data.getScore();
                        Toast.makeText(this, "點擊正確，獲得"+String.valueOf(data.getScore())+"分",Toast.LENGTH_SHORT).show();
                    } else if (data.getName().equals("炸彈")) {
                        now_scoce -= 50;
                        Toast.makeText(this, "點擊到炸彈，扣除 50 分", Toast.LENGTH_SHORT).show();
                    } else {
                        now_scoce -= 30;
                        Toast.makeText(this, "點擊錯誤水果，扣除 30 分", Toast.LENGTH_SHORT).show();
                    }
                    // 避免分數為負值
                    if (now_scoce < 0){
                        now_scoce = 0;
                    }
                    score_textView.setText(String.valueOf(now_scoce));
                    // TODO: 到下個場景
                    if (now_scoce >= target_sccre){
                        Toast.makeText(this, "恭喜已達成" + String.valueOf(target_sccre) + "分的目標，請前往下一關", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("game1", true); // key 是 "done"，value 是 true
                        editor.apply();
                        Intent intent = new Intent(this, Game.class);
                        startActivity(intent);
                    }
                    getRandomItem();
                    putAllImages();
                });
                image_canvas.addView(imgBtn);
            }
        });
    }
}