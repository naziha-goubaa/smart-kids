package com.example.kidsmart;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class FlagColorsActivity extends AppCompatActivity {

    ImageView img;
    GridLayout grid;
    TextView txtCountry;

    String[][] flags = {
            {"france", "bleu", "blanc", "rouge"},
            {"italy", "vert", "blanc", "rouge"},
            {"germany", "noir", "rouge", "jaune"}
    };

    Map<String, Integer> flagImages = new HashMap<>();
    Map<String, Integer> colorMap = new HashMap<>();

    String[] current;
    List<String> remaining;

    MediaPlayer mpCorrect, mpWrong;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_flag_colors);

        img = findViewById(R.id.imgFlag);
        grid = findViewById(R.id.gridColors);
        txtCountry = findViewById(R.id.txtCountry);

        flagImages.put("france", R.drawable.flag_france);
        flagImages.put("italy", R.drawable.flag_italy);
        flagImages.put("germany", R.drawable.flag_germany);

        colorMap.put("bleu", android.R.color.holo_blue_dark);
        colorMap.put("blanc", android.R.color.white);
        colorMap.put("rouge", android.R.color.holo_red_dark);
        colorMap.put("vert", android.R.color.holo_green_dark);
        colorMap.put("noir", android.R.color.black);
        colorMap.put("jaune", android.R.color.holo_orange_light);

        mpCorrect = MediaPlayer.create(this, R.raw.correct);
        mpWrong = MediaPlayer.create(this, R.raw.wrong);

        newFlag();
    }

    void newFlag() {
        current = flags[new Random().nextInt(flags.length)];
        img.setImageResource(flagImages.get(current[0]));

        txtCountry.setText("Couleurs du drapeau : " + current[0].toUpperCase());

        remaining = new ArrayList<>();
        for (int i = 1; i < current.length; i++) remaining.add(current[i]);

        grid.removeAllViews();

        String[] colors = {"bleu", "rouge", "vert", "noir", "blanc", "jaune"};

        for (String c : colors) {
            Button b = new Button(this);
            b.setText(c);
            b.setTextColor(getColor(android.R.color.white));
            b.setBackgroundColor(getColor(colorMap.get(c)));
            b.setAllCaps(false);

            b.setPadding(10, 10, 10, 10);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(15, 15, 15, 15);
            b.setLayoutParams(params);

            b.setOnClickListener(v -> check(c, b));
            grid.addView(b);
        }
    }

    void check(String c, Button btn) {
        if (remaining.contains(c)) {
            remaining.remove(c);

            mpCorrect.start();
            animateCorrect(btn);

            if (remaining.isEmpty()) {
                showPopup();
                newFlag();
            }
        } else {
            mpWrong.start();
            vibrate();
            animateWrong(btn);
        }
    }

    void showPopup() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("Bravo !");
        a.setMessage("Tu as trouvé toutes les couleurs !");
        a.setIcon(R.drawable.star_icon);
        a.setPositiveButton("Continuer", null);
        a.show();
    }

    void animateCorrect(Button b) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(b, "scaleX", 1f, 1.2f, 1f);
        anim.setDuration(300);
        anim.start();
    }

    void animateWrong(Button b) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(b, "translationX", 0, 20, -20, 0);
        anim.setDuration(300);
        anim.start();
    }

    void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE));
            }
    }
}
