package com.example.niubig;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
public class PuzzleGameActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private ArrayList<ImageView> puzzlePieces = new ArrayList<>();
    private int[] originalImageResources = {
            R.drawable.puzzle_piece1, R.drawable.puzzle_piece2, R.drawable.puzzle_piece3,
            R.drawable.puzzle_piece4, R.drawable.puzzle_piece5, R.drawable.puzzle_piece6,
            R.drawable.puzzle_piece7, R.drawable.puzzle_piece8, R.drawable.puzzle_piece9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game);

        gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setRowCount(3);
        gridLayout.setColumnCount(3);

        // 1. 建立所有 ImageView 並同時掛上 Touch + Drag listener
        for (int i = 0; i < 9; i++) {
            final ImageView imageView = new ImageView(this);
            imageView.setImageResource(originalImageResources[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setTag(originalImageResources[i]);

            // 排版參數
            GridLayout.Spec rowSpec = GridLayout.spec(i / 3, 1f);
            GridLayout.Spec colSpec = GridLayout.spec(i % 3, 1f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
            params.width = 0;
            params.height = 0;
            params.setGravity(Gravity.FILL);
            params.setMargins(2,2,2,2);
            imageView.setLayoutParams(params);

            // 1. start drag
            imageView.setOnTouchListener((v, ev) -> {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    v.startDragAndDrop(null, new View.DragShadowBuilder(v), v, 0);
                }
                return true;
            });
            // 2. 接 drop ，交換位置並檢查是否完成
            imageView.setOnDragListener((v, ev) -> {
                if (ev.getAction() == DragEvent.ACTION_DROP) {
                    ImageView dragged = (ImageView) ev.getLocalState();
                    ImageView target = (ImageView) v;
                    swapPieces(dragged, target);
                    checkIfPuzzleSolved();
                }
                return true;
            });

            puzzlePieces.add(imageView);
            gridLayout.addView(imageView);
        }

        // 3. 初始洗牌
        shufflePuzzle();
    }

    private void shufflePuzzle() {
        Collections.shuffle(puzzlePieces);
        gridLayout.removeAllViews();

        for (int i = 0; i < puzzlePieces.size(); i++) {
            ImageView piece = puzzlePieces.get(i);
            // 重設同樣帶 Gravity.FILL 的 LayoutParams
            GridLayout.Spec rowSpec = GridLayout.spec(i / 3, 1f);
            GridLayout.Spec colSpec = GridLayout.spec(i % 3, 1f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
            params.width = 0;
            params.height = 0;
            params.setGravity(Gravity.FILL);
            params.setMargins(2,2,2,2);
            piece.setLayoutParams(params);
            gridLayout.addView(piece);
        }
    }

    private void swapPieces(ImageView first, ImageView second) {
        int firstTag = (int) first.getTag();
        int secondTag = (int) second.getTag();
        first.setImageResource(secondTag);
        second.setImageResource(firstTag);
        first.setTag(secondTag);
        second.setTag(firstTag);

        // 同步更新 List 裡的位置（雖然這版不再直接用它檢查）
        int i1 = puzzlePieces.indexOf(first);
        int i2 = puzzlePieces.indexOf(second);
        puzzlePieces.set(i1, second);
        puzzlePieces.set(i2, first);
    }

    private void checkIfPuzzleSolved() {
        // 直接檢查 gridLayout child 序號，確保和 original 一樣
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView iv = (ImageView) gridLayout.getChildAt(i);
            if ((int) iv.getTag() != originalImageResources[i]) {
                return;  // 有一格不對，就直接回
            }
        }
        Toast.makeText(this, "拼圖完成！", Toast.LENGTH_LONG).show();
    }
}

