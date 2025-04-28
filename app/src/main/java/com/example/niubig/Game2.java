package com.example.niubig;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

public class Game2 extends AppCompatActivity {
    TextView disShow_TextView;
    private GPSHelper gpsHelper;
    Float distance;
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

        disShow_TextView = findViewById(R.id.disShow_TextView);
        gpsHelper = new GPSHelper(this);
        showDistance();
    }

    public void back(View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void onClickCheckMap(View view) {
        double latitude = 24.634103634953988;
        double longitude = 121.7168691171622;
        String label = "石板屋";
        String query = latitude + "," + longitude + "(" + label + ")";
        String uriString = "geo:0,0?q=" + Uri.encode(query);
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Uri playStoreUri = Uri.parse("market://details?id=com.google.android.apps.maps");
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
            if (playStoreIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(playStoreIntent);
            } else {
                Uri webUri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                startActivity(new Intent(Intent.ACTION_VIEW, webUri));
            }
        }
    }

    public void showDistance() {
        gpsHelper.start((latitude, longitude) -> {
            Log.d("Game2", "GPS: " + latitude + ", " + longitude);
            double targetLat = 24.634103634953988;
            double targetLon = 121.7168691171622;
            distance = GPSHelper.calculateDistance(latitude, longitude, targetLat, targetLon);
            String disShowString = String.format("與目的地直線距離: %.2f 公尺", distance);
            disShow_TextView.setText(disShowString);
            Log.d("Game2", "距離台北101約 " + distance + " 公尺");
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        gpsHelper.handlePermissionResult(requestCode, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsHelper.stop();
    }
    public void onClickStartGame(View view) {
        // 檢查是否有 GPS 權限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "請先開啟 GPS 授權", Toast.LENGTH_SHORT).show();
            // 跳轉回 Game 畫面
            startActivity(new Intent(this, Game.class));
            return;
        } else if(distance != null && distance <= 100 ) {
            startActivity(new Intent(this, QuizGameActivity.class));
        } else {
            Toast.makeText(this, "距離石板屋 100 公尺內即可開始遊戲。", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, QuizGameActivity.class));
        }
    }
}
