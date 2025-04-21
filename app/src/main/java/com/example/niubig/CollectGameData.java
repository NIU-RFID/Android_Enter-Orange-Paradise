package com.example.niubig;

import java.io.Serializable;

public class CollectGameData implements Serializable {
    public int score = 0;
    public boolean isGameOver = false;

    public void collectCleanWater() {
        score += 10;
        checkWin();
    }

    public void hitDirtyWater() {
        score -= 5;
        if (score < 0) score = 0;
    }

    public void checkWin() {
        if (score >= 100) {
            isGameOver = true;
        }
    }
}
