package com.example.niubig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Arrays;

public class QuizGameActivity extends AppCompatActivity {
    ArrayList<QuizGameData> QA_list;
    LinearLayout quiz_LinearLayout;
    ImageView textView_information;
    Button btn_startGame;
    TextView problem_textView;

    int currentIndex = 0; // 當前題號
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Declare MediaPlayer for sound effects
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_game);

        // Initialize MediaPlayer for correct and wrong sounds
        correctSound = MediaPlayer.create(this, R.raw.win); // Replace with actual file name "win.mp3"
        wrongSound = MediaPlayer.create(this, R.raw.loss); // Replace with actual file name "loss.mp3"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init_data();
    }

    private void init_data() {
        QA_list = new ArrayList<>();
        QA_list.add(new QuizGameData("1. 石板屋是用什麼石頭做的?",
                new ArrayList<>(Arrays.asList("黑石板", "白石板", "綠石板", "紅石磚")), 0));
        QA_list.add(new QuizGameData("2. 石板屋居住時不會出現甚麼問題?",
                new ArrayList<>(Arrays.asList("衛生條件不佳", "房屋漏水", "光線不足", "通風差")), 3));

        textView_information = findViewById(R.id.information_textView);
        btn_startGame = findViewById(R.id.startGame);
        textView_information.setVisibility(View.VISIBLE);
        btn_startGame.setVisibility(View.VISIBLE);

        quiz_LinearLayout = findViewById(R.id.quiz_layout);
        quiz_LinearLayout.setVisibility(View.GONE);

        problem_textView = findViewById(R.id.problem_textView);
        sharedPreferences = getSharedPreferences("isDone", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void hideInformationLayout() {
        textView_information.setVisibility(View.GONE);
        btn_startGame.setVisibility(View.GONE);
        quiz_LinearLayout.setVisibility(View.VISIBLE);
    }

    private void openInformationLayout() {
        textView_information.setVisibility(View.VISIBLE);
        btn_startGame.setVisibility(View.VISIBLE);
        quiz_LinearLayout.setVisibility(View.GONE);
    }

    public void next_question() {
        currentIndex++;
        if (currentIndex < QA_list.size()) {
            putProblem(currentIndex);
        } else {
            Toast.makeText(this, "恭喜你全部答對！", Toast.LENGTH_LONG).show();
            editor.putBoolean("game2", true); // key 是 "done"，value 是 true
            editor.apply();
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
        }
    }

    public void back_to_information() {
        Toast.makeText(this, "回答錯誤~ 再次挑戰吧!", Toast.LENGTH_SHORT).show();
        openInformationLayout();
    }

    private void putProblem(int index) {
        quiz_LinearLayout.removeAllViews(); // 清除前一題的選項

        QuizGameData qa = QA_list.get(index);
        TextView problem = new TextView(this);
        problem.setText(qa.getQuestion());
        problem.setTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Large);
        problem.setTextColor(Color.parseColor("#000000"));
        quiz_LinearLayout.addView(problem);

        if (qa.getOptions().size() > 1) {
            for (int i = 0; i < qa.getOptions().size(); i++) {
                MaterialButton btn = new MaterialButton(this); // 換成 MaterialButton
                btn.setText(qa.getOptions().get(i));
                btn.setTextSize(20); // 設定字體大小，單位是 sp
                btn.setStrokeColor(ColorStateList.valueOf(Color.BLACK)); // 邊框顏色
                int finalI = i;
                btn.setOnClickListener(v -> {
                    if (finalI == qa.getAnswer_index()) {
                        Toast.makeText(this, "答對了！", Toast.LENGTH_SHORT).show();
                        playCorrectSound();  // 播放正確音效
                        next_question();
                    } else {
                        back_to_information();
                        playWrongSound();  // 播放錯誤音效
                    }
                });
                quiz_LinearLayout.addView(btn);
            }
        } else {
            Toast.makeText(this, "Question's Options 應該要大於 1 個", Toast.LENGTH_SHORT).show();
        }
    }

    private void playCorrectSound() {
        if (correctSound != null) {
            correctSound.start();
        }
    }

    private void playWrongSound() {
        if (wrongSound != null) {
            wrongSound.start();
        }
    }

    private void quizStart() {
        currentIndex = 0;
        putProblem(currentIndex);
    }

    public void onClickStartGame(View view) {
        hideInformationLayout();
        quizStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (correctSound != null) {
            correctSound.release();
        }
        if (wrongSound != null) {
            wrongSound.release();
        }
    }
}
