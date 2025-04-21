package com.example.niubig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class CollectGameActivity extends AppCompatActivity {

    int target_resId, dodge_redId;
    TextView score_TextView;
    FrameLayout gameArea;
    ImageView bucket;
    CollectGameData gameData;
    Handler handler = new Handler();
    Runnable spawnRunnable;
    Random random = new Random();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_game);

        init_data();
        startSpawning();
        setupBucketControls();
    }

    public void init_data(){
        target_resId = getResources().getIdentifier("clean_water", "drawable", getPackageName());
        dodge_redId = getResources().getIdentifier("dirty_water", "drawable", getPackageName());
        score_TextView = findViewById(R.id.score_textView);
        gameArea = findViewById(R.id.game_area);
        bucket = findViewById(R.id.bucket);
        gameData = new CollectGameData();
        sharedPreferences = getSharedPreferences("isDone", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void startSpawning() {
        spawnRunnable = new Runnable() {
            @Override
            public void run() {
                spawnWater();
                if (!gameData.isGameOver) {
                    handler.postDelayed(this, 800);
                } else {
                    Toast.makeText(CollectGameActivity.this, "你通關了！", Toast.LENGTH_LONG).show();
                    editor.putBoolean("game3", true); // key 是 "done"，value 是 true
                    editor.apply();
                    Intent intent = new Intent(CollectGameActivity.this, Game.class);
                    startActivity(intent);
                }
            }
        };
        handler.post(spawnRunnable);
    }

    public void spawnWater() {
        final ImageView drop = new ImageView(this);
        boolean isClean = random.nextBoolean();
        drop.setImageResource(isClean ? target_resId : dodge_redId);

        int dropSize = 100;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dropSize, dropSize);
        int x = random.nextInt(gameArea.getWidth() - dropSize);
        drop.setX(x);
        drop.setY(0);
        gameArea.addView(drop, params);

        final Handler dropHandler = new Handler();
        final Runnable dropRunnable = new Runnable() {
            float speed = 30f; // 每次往下移動的距離
            @Override
            public void run() {
                if (drop.getY() >= gameArea.getHeight()) {
                    gameArea.removeView(drop); // 掉出畫面就移除
                    return;
                }

                drop.setY(drop.getY() + speed);

                if (checkCollision(drop, bucket)) {
                    if (isClean) {
                        gameData.collectCleanWater();
                    } else {
                        gameData.hitDirtyWater();
                    }
                    updateScore();
                    gameArea.removeView(drop);
                    return;
                }

                dropHandler.postDelayed(this, 30); // 每 30ms 移動一次
            }
        };
        dropHandler.post(dropRunnable);
    }


    public void updateScore() {
        score_TextView.setText("分數：" + gameData.score);
    }

    public boolean checkCollision(View a, View b) {
        Rect rect1 = new Rect();
        a.getHitRect(rect1);
        Rect rect2 = new Rect();
        b.getHitRect(rect2);
        return Rect.intersects(rect1, rect2);
    }

    public void setupBucketControls() {
        bucket.setOnTouchListener((v, event) -> {
            float newX = event.getRawX() - bucket.getWidth() / 2f;
            if (newX >= 0 && newX <= gameArea.getWidth() - bucket.getWidth()) {
                bucket.setX(newX);
            }
            return true;
        });
    }
}
