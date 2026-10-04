/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité implémente un mini-jeu éducatif de correspondance "Image-Mot" pour les enfants.
 * L'objectif est de faire glisser chaque image sur le mot correspondant.
 * Le jeu fournit un retour sonore (correct/wrong) et affiche un message lorsqu'un tour est terminé.
 *
 * Détails :
 * - Charge les données depuis un JSON (fruits ou autre catégorie).
 * - Affiche un ensemble d'images et leurs noms mélangés.
 * - Permet de glisser-déposer une image sur le mot correspondant.
 * - Vérifie la correspondance et donne un retour interactif.
 * - Passe automatiquement au tour suivant lorsque toutes les correspondances sont correctes.
 */

package com.example.kidsmart;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidsmart.utils.JSONLoader;
import com.example.kidsmart.utils.SoundPlayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchImageWordActivity extends AppCompatActivity {

    private LinearLayout colImages, colWords; // Colonnes pour afficher les images et les mots
    private List<JSONObject> items;           // Liste des éléments (image + mot)
    private SoundPlayer sp;                   // Lecteur sonore pour correct/wrong

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_image_word);

        // Récupération des vues
        colImages = findViewById(R.id.colImages);
        colWords = findViewById(R.id.colWords);

        // Initialisation du lecteur sonore
        sp = new SoundPlayer(this);

        // Charger JSON depuis l'Intent ou fallback local
        String jsonData = getIntent().getStringExtra("jsonData");
        if(jsonData != null) {
            items = new ArrayList<>();
            try {
                org.json.JSONArray arr = new org.json.JSONArray(jsonData);
                for (int i=0; i<arr.length(); i++) {
                    items.add(arr.getJSONObject(i));
                }
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            items = JSONLoader.loadArray(this, "data/fruits.json");
        }

        // Démarrer le premier tour
        startRound();
    }

    /**
     * Prépare et affiche un tour de jeu avec des images et mots mélangés.
     */
    private void startRound() {
        colImages.removeAllViews();
        colWords.removeAllViews();

        // Mélanger les éléments et sélectionner un sous-ensemble
        List<JSONObject> copy = new ArrayList<>(items);
        Collections.shuffle(copy);
        List<JSONObject> pick = new ArrayList<>(copy.subList(0, Math.min(4, copy.size())));

        // --- Images ---
        for (JSONObject p : pick) {
            ImageView iv = new ImageView(this);

            int imgId = getResources().getIdentifier(
                    p.optString("image"),
                    "drawable",
                    getPackageName()
            );

            iv.setImageResource(imgId);
            iv.setTag(p.optString("name")); // Tag pour identifier la correspondance

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, 250);
            params.setMargins(12,12,12,12);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Réduction alpha au clic (feedback visuel)
            iv.setOnClickListener(v -> v.setAlpha(0.5f));

            // Gestion du drag & drop
            iv.setOnLongClickListener(v -> {
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(null, shadow, v, 0);
                } else {
                    v.startDrag(null, shadow, v, 0);
                }
                return true;
            });

            colImages.addView(iv);
        }

        // --- Mots ---
        List<JSONObject> words = new ArrayList<>(pick);
        Collections.shuffle(words);

        for (JSONObject w : words) {
            TextView tv = new TextView(this);
            tv.setText(w.optString("name"));
            tv.setTag(w.optString("name")); // Tag pour identifier la correspondance

            tv.setTextSize(20);
            tv.setPadding(16,16,16,16);
            tv.setBackgroundResource(R.drawable.bg_word_card);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10,10,10,10);
            tv.setLayoutParams(params);

            // Réduction alpha au clic (feedback visuel)
            tv.setOnClickListener(v -> v.setAlpha(0.5f));

            // Listener pour gérer le drop des images sur le mot
            tv.setOnDragListener((v, e) -> {
                switch (e.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(0xFFCCE5FF); // survol
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundResource(R.drawable.bg_word_card);
                        break;
                    case DragEvent.ACTION_DROP:
                        v.setBackgroundResource(R.drawable.bg_word_card);
                        View dragged = (View) e.getLocalState();
                        String draggedTag = (String) dragged.getTag();
                        String targetTag = (String) v.getTag();

                        // Vérification de correspondance
                        if(draggedTag.equals(targetTag)) {
                            sp.playCorrect();
                            v.setVisibility(View.INVISIBLE);
                            dragged.setVisibility(View.INVISIBLE);
                            checkWin();
                        } else {
                            sp.playWrong();
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setBackgroundResource(R.drawable.bg_word_card);
                        break;
                }
                return true;
            });

            colWords.addView(tv);
        }
    }

    /**
     * Vérifie si toutes les correspondances ont été trouvées.
     * Si oui, affiche un message et redémarre le tour.
     */
    private void checkWin() {
        boolean allDone = true;
        for(int i=0; i<colWords.getChildCount(); i++) {
            if(colWords.getChildAt(i).getVisibility() == View.VISIBLE) {
                allDone = false;
                break;
            }
        }
        if(allDone) {
            new AlertDialog.Builder(this)
                    .setTitle("Bravo ! 🎉")
                    .setMessage("Toutes les associations sont correctes !")
                    .setPositiveButton("Niveau suivant", (d,i) -> startRound())
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.release(); // Libération du lecteur sonore
    }
}
