package com.example.kidsmart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBarSplash);

        // Simuler la progression
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(() -> progressBar.setProgress(progressStatus));
                    try {
                        Thread.sleep(20); // vitesse de progression
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Lancer l'activité principale après le splash
                Intent intent = new Intent(SplashActivity.this, MenuActivity.class); // Remplace par ton menu
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
