package com.example.niubig;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Game2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 先檢查權限問題
        GPSHelper.checkAndRequestPermission(this);
    }
    public void back(View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }
    @SuppressLint("QueryPermissionsNeeded")
    public void onClickCheckMap(View view){
        double latitude = 24.634147294374543;
        double longitude = 121.71689365002302;
        String label = "石板屋";
        String uriBegin = "geo:0,0?q=";
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + encodedQuery;
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // 裝置未安裝 Google 地圖，導引使用者前往 Google Play 商店
            Uri playStoreUri = Uri.parse("market://details?id=com.google.android.apps.maps");
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
            if (playStoreIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(playStoreIntent);
            } else {
                // 若無法開啟 Play 商店，改為開啟瀏覽器連結
                Uri webUri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(webIntent);
            }
        }
    }
    // 暫時
    public void onClickStartGame(View view){
        if (!GPSHelper.hasPermission(this)){
            Toast.makeText(this, "請先開啟 GPS 授權", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
        }else{
            // 進入第二關遊戲中
        }
    }
}