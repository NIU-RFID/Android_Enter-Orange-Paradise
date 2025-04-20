package com.example.niubig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Game extends AppCompatActivity {
    SharedPreferences sharedPreferences; // = getSharedPreferences("isDone", MODE_PRIVATE);
    boolean game1_isDone, game2_isDone, game3_isDone, game4_isDone, game5_isDone;
    ImageView image_1, image_2, image_3, image_4, image_5;
    int done_resId, not_redId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init_data();
        checkDone();
    }
    public void init_data(){
        sharedPreferences = getSharedPreferences("isDone", MODE_PRIVATE);
        game1_isDone = sharedPreferences.getBoolean("game1", false);
        game2_isDone = sharedPreferences.getBoolean("game2", false);
        game3_isDone = sharedPreferences.getBoolean("game3", false);
        game4_isDone = sharedPreferences.getBoolean("game4", false);
        game5_isDone = sharedPreferences.getBoolean("game5", false);
        image_1 = findViewById(R.id.imageView);
        image_2 = findViewById(R.id.imageView2);
        image_3 = findViewById(R.id.imageView3);
        image_4 = findViewById(R.id.imageView4);
        image_5 = findViewById(R.id.imageView5);
        done_resId = getResources().getIdentifier("done", "drawable", getPackageName());
        not_redId = getResources().getIdentifier("not", "drawable", getPackageName());
    }
    public void checkDone(){
        if(game1_isDone){image_1.setImageResource(done_resId);}else{image_1.setImageResource(not_redId);}
        if(game2_isDone){image_2.setImageResource(done_resId);}else{image_2.setImageResource(not_redId);}
        if(game3_isDone){image_3.setImageResource(done_resId);}else{image_3.setImageResource(not_redId);}
        if(game4_isDone){image_4.setImageResource(done_resId);}else{image_4.setImageResource(not_redId);}
        if(game5_isDone){image_5.setImageResource(done_resId);}else{image_5.setImageResource(not_redId);}
    }
    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickStartGame1(View view){
        Intent intent = new Intent(this, Game1.class);
        startActivity(intent);
    }
    public void onClickStartGame2(View view){
        Intent intent = new Intent(this, Game2.class);
        startActivity(intent);
    }
    public void onClickStartGame3(View view){
        Intent intent = new Intent(this, Game3.class);
        startActivity(intent);
    }
    public void onClickStartGame4(View view){
        Intent intent = new Intent(this, Game4.class);
        startActivity(intent);
    }
    public void onClickStartGame5(View view){
        Intent intent = new Intent(this, Game5.class);
        startActivity(intent);
    }
}