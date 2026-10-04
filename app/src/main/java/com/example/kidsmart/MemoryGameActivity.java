package com.example.kidsmart;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidsmart.adapters.ImageAdapter;
import com.example.kidsmart.utils.SoundPlayer;
import com.example.kidsmart.utils.TTSManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameActivity extends AppCompatActivity {

    private GridView grid;
    private TextView tvScore, tvTimer;

    private int firstIndex = -1;
    private boolean locked = false;
    private int score = 0;

    private List<Integer> images = new ArrayList<>();
    private List<Integer> gridImages = new ArrayList<>();
    private ImageAdapter adapter;

    private TTSManager tts;
    private SoundPlayer sound;

    private Handler timerHandler = new Handler();
    private int secondsElapsed = 0;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            secondsElapsed++;
            tvTimer.setText("Temps: " + secondsElapsed + "s");
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        tts = new TTSManager(this);
        sound = new SoundPlayer(this);

        grid = findViewById(R.id.gridView);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);

        grid.setNumColumns(2);

        // Charger JSON depuis intent
        String jsonAnimals = getIntent().getStringExtra("jsonAnimals");
        if (jsonAnimals == null) jsonAnimals = "[]";

        try {
            JSONArray jsonArray = new JSONArray(jsonAnimals);
            for (int i = 0; i < jsonArray.length(); i++) {
                String imageName = jsonArray.getJSONObject(i).getString("image");
                int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                images.add(resId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initGame();


        grid.setOnItemClickListener((parent, view, position, id) -> handleClick(position));
    }

    private void initGame() {
        gridImages.clear();
        gridImages.addAll(images);
        gridImages.addAll(images);
        Collections.shuffle(gridImages);

        firstIndex = -1;
        locked = false;
        score = 0;
        tvScore.setText("Score: " + score);

        // Timer reset
        secondsElapsed = 0;
        tvTimer.setText("Temps: 0s");
        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.postDelayed(timerRunnable, 1000);

        adapter = new ImageAdapter(this, gridImages);
        grid.setAdapter(adapter);
    }

    private void handleClick(int pos) {
        if (locked || adapter.isFlipped(pos)) return;

        adapter.flip(pos);

        if (firstIndex == -1) {
            firstIndex = pos;
        } else {
            locked = true;
            int secondIndex = pos;

            new Handler().postDelayed(() -> checkMatch(firstIndex, secondIndex), 500);
        }
    }

    private void checkMatch(int index1, int index2) {
        if (gridImages.get(index1).equals(gridImages.get(index2))) {
            score++;
            tvScore.setText("Score: " + score);
            sound.playCorrect();
            tts.speak("Bravo !");
        } else {
            adapter.flipBack(index1);
            adapter.flipBack(index2);
            sound.playWrong();
        }

        firstIndex = -1;
        locked = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
