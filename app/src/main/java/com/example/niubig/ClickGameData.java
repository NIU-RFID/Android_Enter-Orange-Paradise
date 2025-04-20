package com.example.niubig;
import java.io.Serializable;

public class ClickGameData implements Serializable{
    private String name;
    private String image_path;
    private int score;

    // Constructor
    public ClickGameData(String name, String image_path, int score) {
        this.name = name;
        this.image_path = image_path;
        this.score = score;
    }

    // Getters
    public String getImagePath() {
        return image_path;
    }
    public String getName(){
        return name;
    }
    public int getScore() {
        return score;
    }
}
