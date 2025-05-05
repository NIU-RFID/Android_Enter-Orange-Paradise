package com.example.niubig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class CollectGameActivity extends AppCompatActivity {

    int targetResId, dodgeRedId;
    TextView scoreTextView;
    FrameLayout gameArea;
    ImageView bucket;
    CollectGameData gameData;
    Handler handler = new Handler();
    Runnable spawnRunnable;
    Random random = new Random();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // 音效播放器
    MediaPlayer winSound;
    MediaPlayer lossSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_game);

        initData();
        startSpawning();
        setupBucketControls();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 釋放音效資源
        if (winSound != null) {
            winSound.release();
        }
        if (lossSound != null) {
            lossSound.release();
        }
    }

    private void initData() {
        targetResId = getResources().getIdentifier("clean_water", "drawable", getPackageName());
        dodgeRedId = getResources().getIdentifier("dirty_water", "drawable", getPackageName());
        scoreTextView = findViewById(R.id.score_textView);
        gameArea = findViewById(R.id.game_area);
        bucket = findViewById(R.id.bucket);
        gameData = new CollectGameData();
        sharedPreferences = getSharedPreferences("isDone", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // 載入音效（注意：res/raw/win.mp3 和 res/raw/loss.mp3）
        winSound = MediaPlayer.create(this, R.raw.win);
        lossSound = MediaPlayer.create(this, R.raw.loss);
    }

    private void startSpawning() {
        spawnRunnable = new Runnable() {
            @Override
            public void run() {
                spawnWater();
                if (!gameData.isGameOver) {
                    handler.postDelayed(this, 800);
                } else {
                    Toast.makeText(CollectGameActivity.this, "你通關了！", Toast.LENGTH_LONG).show();
                    editor.putBoolean("game3", true);
                    editor.apply();
                    Intent intent = new Intent(CollectGameActivity.this, Game.class);
                    startActivity(intent);
                    finish(); // 防止返回
                }
            }
        };
        handler.post(spawnRunnable);
    }

    private void spawnWater() {
        final ImageView drop = new ImageView(this);
        boolean isClean = random.nextBoolean();
        drop.setImageResource(isClean ? targetResId : dodgeRedId);

        int dropSize = 100;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dropSize, dropSize);
        int x = random.nextInt(Math.max(1, gameArea.getWidth() - dropSize)); // 防止 width=0 crash
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
                        playWinSound(); // 播放撿到乾淨水音效
                    } else {
                        gameData.hitDirtyWater();
                        playLossSound(); // 播放撿到髒水音效
                    }
                    updateScore();
                    gameArea.removeView(drop);
                    return;
                }

                dropHandler.postDelayed(this, 30); // 每30ms移動一次
            }
        };
        dropHandler.post(dropRunnable);
    }

    private void updateScore() {
        scoreTextView.setText("分數：" + gameData.score);
    }

    private boolean checkCollision(View a, View b) {
        Rect rect1 = new Rect();
        a.getHitRect(rect1);
        Rect rect2 = new Rect();
        b.getHitRect(rect2);
        return Rect.intersects(rect1, rect2);
    }

    private void setupBucketControls() {
        bucket.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float newX = event.getRawX() - bucket.getWidth() / 2f;
                if (newX >= 0 && newX <= gameArea.getWidth() - bucket.getWidth()) {
                    bucket.setX(newX);
                }
            }
            return true;
        });
    }

    // 播放正確 (撿到乾淨水)
    private void playWinSound() {
        if (winSound != null) {
            winSound.start();
        }
    }

    // 播放錯誤 (撿到髒水)
    private void playLossSound() {
        if (lossSound != null) {
            lossSound.start();
        }
    }
}
