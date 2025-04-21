package com.example.niubig;

import java.io.Serializable;

public class MemoryGameData implements Serializable {

    private int id;
    private int imageResId;
    private boolean isText;
    private boolean isFaceUp = false;
    private boolean isMatched = false;

    public MemoryGameData(int id, int imageResId, boolean isText) {
        this.id = id;
        this.imageResId = imageResId;
        this.isText = isText;
    }

    public int getId() { return id; }
    public int getImageResId() { return imageResId; }
    public boolean isText() { return isText; }
    public boolean isFaceUp() { return isFaceUp; }
    public boolean isMatched() { return isMatched; }

    public void setFaceUp(boolean faceUp) { isFaceUp = faceUp; }
    public void setMatched(boolean matched) { isMatched = matched; }
}
