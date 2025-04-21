package com.example.niubig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameActivity extends AppCompatActivity {

    private List<MemoryGameData> cards = new ArrayList<>();
    private List<ImageView> imageViews = new ArrayList<>();
    private MemoryGameData firstFlipped = null;
    private boolean isBusy = false;

    private final int pairCount = 4; // 🔧 可調整你有幾對卡牌（記得 drawable 資源要齊）
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
        sharedPreferences = getSharedPreferences("isDone", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        GridLayout grid = findViewById(R.id.memory_grid);
        initializeCards();

        int totalCards = cards.size();
        int columnCount = 2; // ✅ 每排兩張卡牌
        int rowCount = (int) Math.ceil((double) totalCards / columnCount);
        grid.setColumnCount(columnCount);
        grid.setRowCount(rowCount);

        // 取得螢幕寬度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        // 每張卡片寬度為螢幕寬度的 0.75 / 2
        int cardSize = (int) ((screenWidth * 0.75f) / columnCount);

        for (int i = 0; i < totalCards; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.fold_card);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setAdjustViewBounds(true);
            iv.setPadding(8, 8, 8, 8);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = cardSize;
            params.height = cardSize;
            iv.setLayoutParams(params);

            int finalI = i;
            iv.setOnClickListener(v -> onCardClick(finalI));
            grid.addView(iv);
            imageViews.add(iv);
        }

    }

    private void initializeCards() {
        for (int i = 1; i <= pairCount; i++) {
            int imageResId = getResources().getIdentifier("memory_" + i + "_image", "drawable", getPackageName());
            int textResId = getResources().getIdentifier("memory_" + i + "_text", "drawable", getPackageName());

            if (imageResId != 0 && textResId != 0) {
                cards.add(new MemoryGameData(i, imageResId, false)); // image
                cards.add(new MemoryGameData(i, textResId, true));   // text
            }
        }

        Collections.shuffle(cards);
    }

    private void onCardClick(int index) {
        if (isBusy) return;
        MemoryGameData card = cards.get(index);
        if (card.isFaceUp() || card.isMatched()) return;

        card.setFaceUp(true);
        imageViews.get(index).setImageResource(card.getImageResId());

        if (firstFlipped == null) {
            firstFlipped = card;
        } else {
            isBusy = true;
            MemoryGameData secondFlipped = card;
            new Handler().postDelayed(() -> {
                if (secondFlipped.getId() == firstFlipped.getId() &&
                        secondFlipped.isText() != firstFlipped.isText()) {
                    secondFlipped.setMatched(true);
                    firstFlipped.setMatched(true);

                    // 👇 檢查是否全部卡牌都 matched
                    if (isGameCompleted()) {
                        Toast.makeText(MemoryGameActivity.this, "您已通關!", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("game5", true);
                        editor.apply();
                        Intent intent = new Intent(this, Game.class);
                        startActivity(intent);
                    }
                } else {
                    secondFlipped.setFaceUp(false);
                    firstFlipped.setFaceUp(false);
                    updateCardImages();
                }
                firstFlipped = null;
                isBusy = false;
            }, 1000);
        }
    }

    private void updateCardImages() {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).isFaceUp() || cards.get(i).isMatched()) {
                imageViews.get(i).setImageResource(cards.get(i).getImageResId());
            } else {
                imageViews.get(i).setImageResource(R.drawable.fold_card);
            }
        }
    }
    private boolean isGameCompleted() {
        for (MemoryGameData card : cards) {
            if (!card.isMatched()) {
                return false;
            }
        }
        return true;
    }

}
