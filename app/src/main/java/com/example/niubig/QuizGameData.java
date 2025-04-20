package com.example.niubig;
import java.io.Serializable;
import java.util.ArrayList;

public class QuizGameData implements Serializable {
    private String question;
    private ArrayList<String> options;
    private int answer_index; // 原始序列的答案 index
    private String answer; // 被動變更成答案
    public QuizGameData(String question, ArrayList<String> options, int answer_index){
        this.question = question;
        this.options = options;
        this.answer_index = answer_index;
        this.answer = options.get(answer_index);
    }
    public String getQuestion(){
        return this.question;
    }
    public ArrayList<String> getOptions(){
        return this.options;
    }
    public int getAnswer_index(){
        return this.answer_index;
    }
    public String getAnswer(){
        return this.answer;
    }
}
