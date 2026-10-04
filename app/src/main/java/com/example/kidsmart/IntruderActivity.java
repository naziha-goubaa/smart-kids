/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité implémente un mini-jeu éducatif appelé "Intrus" destiné aux enfants.
 * Le jeu consiste à afficher un ensemble d'images d'animaux sur une grille et demander
 * à l'enfant de trouver l'animal qui est différent des autres (l'intrus).
 * L'application fournit un retour sonore et visuel selon que la réponse est correcte ou non,
 * ainsi qu'une vibration en cas d'erreur.
 *
 * Détails :
 * - Charge un JSON contenant les données des animaux.
 * - Affiche 4 images : 3 identiques et 1 intrus.
 * - Gère les clics pour détecter si l'utilisateur a choisi l'intrus.
 * - Fournit des sons et des dialogues pour guider l'enfant.
 * - Prévoit des fallback (placeholder, vérification des vues, gestion d'erreurs).
 */

package com.example.kidsmart;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidsmart.utils.JSONLoader;
import com.example.kidsmart.utils.SoundPlayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IntruderActivity extends AppCompatActivity {

    private static final String TAG = "IntruderActivity";

    private GridLayout grid;          // Grille pour afficher les images
    private TextView txtTitle;        // Titre du jeu
    private List<JSONObject> animals; // Liste des animaux chargés depuis JSON
    private SoundPlayer sp;           // Lecteur sonore pour sons correct/erreur

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);

        // Assure que le bon layout est utilisé
        setContentView(R.layout.activity_intruder);

        // Récupération des vues
        grid = findViewById(R.id.gridLayout);
        txtTitle = findViewById(R.id.txtTitle);

        // Vérification defensive
        if (grid == null || txtTitle == null) {
            Log.e(TAG, "Layout error: views not found (gridLayout or txtTitle is null)");
            Toast.makeText(this, "Erreur d'interface : veuillez vérifier le layout.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Initialisation du lecteur sonore
        try {
            sp = new SoundPlayer(this);
        } catch (Exception ex) {
            Log.w(TAG, "Impossible d'initialiser SoundPlayer", ex);
            sp = null;
        }

        // Chargement des données JSON des animaux
        try {
            animals = JSONLoader.loadArray(this, "data/animals.json");
        } catch (Exception ex) {
            Log.e(TAG, "Erreur lors du chargement du JSON", ex);
            animals = null;
        }

        // Vérification de la validité des données
        if (animals == null || animals.size() < 2) {
            Log.e(TAG, "Les données des animaux sont manquantes ou insuffisantes");
            new AlertDialog.Builder(this)
                    .setTitle("Données manquantes")
                    .setMessage("Impossible de charger les données du jeu. Assure-toi que `data/animals_simple.json` existe et contient au moins 2 éléments.")
                    .setPositiveButton("OK", (d, i) -> finish())
                    .setCancelable(false)
                    .show();
            return;
        }

        // Préparer le premier tour
        next();
    }

    /**
     * Prépare et affiche le prochain tour du jeu avec un intrus aléatoire.
     */
    private void next() {
        grid.removeAllViews();

        // Copie défensive et mélange de la liste
        List<JSONObject> copy = new ArrayList<>(animals);
        Collections.shuffle(copy);

        // Sélection de l'animal de base et de l'intrus
        JSONObject base = copy.get(0);
        JSONObject intruder = pickIntruder(base, copy);

        if (base == null || intruder == null) {
            Log.e(TAG, "base or intruder is null");
            Toast.makeText(this, "Erreur interne du jeu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Préparer les options : 3 images identiques + 1 intrus
        List<JSONObject> options = new ArrayList<>();
        options.add(base);
        options.add(base);
        options.add(base);
        options.add(intruder);
        Collections.shuffle(options);

        // Création des ImageViews
        for (JSONObject o : options) {
            ImageView iv = new ImageView(this);

            String imageName = o.optString("image", "");
            int imgRes = 0;
            if (!imageName.isEmpty()) {
                imgRes = getResources().getIdentifier(imageName, "drawable", getPackageName());
            }

            if (imgRes == 0) {
                imgRes = R.drawable.placeholder_image;
                Log.w(TAG, "drawable not found for " + imageName + ", using placeholder");
            }

            iv.setImageResource(imgRes);

            // Taille et layout params
            int size = (int) (getResources().getDisplayMetrics().widthPixels / 2.3);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = size;
            params.height = size;
            params.setMargins(14, 14, 14, 14);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Background
            try {
                iv.setBackgroundResource(R.drawable.rounded_shadow);
            } catch (Exception ex) {
                Log.w(TAG, "rounded_shadow drawable missing", ex);
            }

            iv.setPadding(8, 8, 8, 8);

            // Gestion du clic sur l'image
            iv.setOnClickListener(v -> handleClick(o, intruder, iv));

            grid.addView(iv);
        }
    }

    /**
     * Sélectionne un intrus différent de l'animal de base.
     */
    private JSONObject pickIntruder(JSONObject base, List<JSONObject> list) {
        String baseCat = base.optString("category", "");
        for (JSONObject o : list) {
            if (!o.optString("category", "").equals(baseCat)) {
                return o;
            }
        }
        if (list.size() > 1) return list.get(1);
        return base;
    }

    /**
     * Gère le clic sur une image et détermine si l'utilisateur a choisi l'intrus.
     */
    private void handleClick(JSONObject selected, JSONObject intruder, ImageView iv) {
        boolean isIntruder = selected.optString("name").equals(intruder.optString("name"));

        if (isIntruder) {
            if (sp != null) {
                try { sp.playCorrect(); } catch (Exception e) { Log.w(TAG, "playCorrect failed", e); }
            }
            showCorrectDialog(intruder);

        } else {
            if (sp != null) {
                try { sp.playWrong(); } catch (Exception e) { Log.w(TAG, "playWrong failed", e); }
            }
            vibrate();
            showWrongHint();
        }
    }

    private void showCorrectDialog(JSONObject intruder) {
        new AlertDialog.Builder(this)
                .setTitle("Bravo !")
                .setMessage("Tu as trouvé l'intrus : " + intruder.optString("name"))
                .setIcon(R.drawable.correct_icon)
                .setCancelable(false)
                .setPositiveButton("Suivant", (d, i) -> next())
                .show();
    }

    private void showWrongHint() {
        new AlertDialog.Builder(this)
                .setTitle("Oups")
                .setMessage("Ce n'est pas l'intrus. Essaie encore !")
                .setPositiveButton("OK", null)
                .show();
    }

    private void vibrate() {
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (v != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } catch (Exception e) {
            Log.w(TAG, "vibrate failed", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sp != null) {
            try { sp.release(); } catch (Exception e) { Log.w(TAG, "release soundplayer failed", e); }
        }
    }
}
