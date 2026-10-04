package com.example.kidsmart;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidsmart.utils.SoundPlayer;
import com.example.kidsmart.utils.TTSManager;

public class ColoringActivity extends AppCompatActivity {

    private DrawingView draw;
    private Button btnClear;
    private SoundPlayer sp;
    private TTSManager tts;

    // PALETTE DE COULEURS
    private int[] palette = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.CYAN,
            Color.rgb(255,140,0),   // ORANGE
            Color.rgb(128,0,128)    // VIOLET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coloring);

        draw = findViewById(R.id.drawView);
        btnClear = findViewById(R.id.btnClear);

        sp = new SoundPlayer(this);
        tts = new TTSManager(this);

        // ➤ BOUTON EFFACER
        btnClear.setOnClickListener(v -> {
            draw.clear();
            sp.playWrong();
            tts.speak("Effacé !");
        });

        // ➤ GÉNÉRATION DES BOUTONS DE PALETTE
        createPaletteButtons();
    }

    private void createPaletteButtons() {
        LinearLayout paletteBar = findViewById(R.id.paletteBar);

        for (int color : palette) {
            Button b = new Button(this);
            b.setBackgroundColor(color);
            b.setWidth(120);
            b.setHeight(120);

            b.setOnClickListener(v -> {
                draw.setPaintColor(color);
                tts.speak("Couleur changée");
            });

            paletteBar.addView(b);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.release();
        tts.shutdown();
    }
}
