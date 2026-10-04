/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité implémente un jeu éducatif de **reconnaissance des sons pour enfants**.
 * L'objectif est de jouer le son d'un animal et demander à l'enfant de sélectionner l'image correspondante.
 *
 * Détails :
 * - Charge les données JSON contenant le nom, l'image et le son de chaque animal.
 * - Pour chaque round :
 *      • Sélectionne 4 options aléatoires parmi les animaux.
 *      • Choisit un animal cible (target) parmi ces options.
 *      • Affiche les images dans une grille.
 * - L'enfant écoute le son (ou le TTS si le son manquant) et clique sur l'image correspondante.
 * - Fournit un feedback immédiat via pop-up et sons correct/incorrect.
 * - Passe au round suivant automatiquement après une réponse correcte.
 */

package com.example.kidsmart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidsmart.utils.JSONLoader;
import com.example.kidsmart.utils.SoundPlayer;
import com.example.kidsmart.utils.TTSManager;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.media.MediaPlayer;

public class SoundMatchActivity extends AppCompatActivity {

    private Button btnPlay;          // Bouton pour jouer le son cible
    private GridLayout grid;         // Grille contenant les images à sélectionner
    private List<JSONObject> items;  // Liste des animaux chargés depuis JSON
    private JSONObject target;       // Animal cible du round
    private MediaPlayer mp;          // Lecteur audio pour le son
    private SoundPlayer sp;          // Jouer les sons correct/incorrect
    private TTSManager tts;          // Synthèse vocale si le son est absent

    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_sound_match);

        btnPlay = findViewById(R.id.btnPlaySound);
        grid = findViewById(R.id.gridOptions);

        sp = new SoundPlayer(this);
        tts = new TTSManager(this);

        // Charger JSON : entries {"name":"Lion","image":"img_lion","sound":"lion_roar"}
        items = JSONLoader.loadArray(this, "data/animals_sounds.json");

        startRound();  // Démarrer le premier round

        btnPlay.setOnClickListener(v -> playTargetSound());
    }

    /**
     * Prépare un nouveau round du jeu
     */
    private void startRound() {
        if (mp!=null){ mp.release(); mp=null; }
        grid.removeAllViews();

        List<JSONObject> copy = new ArrayList<>(items);
        Collections.shuffle(copy);

        // Choisir jusqu'à 4 options aléatoires
        List<JSONObject> options = copy.subList(0, Math.min(4, copy.size()));
        Collections.shuffle(options);

        // Définir la cible parmi les options
        target = options.get(0);

        for (JSONObject o : options) {
            ImageView iv = new ImageView(this);
            int id = getResId(this, o.optString("image"));
            iv.setImageResource(id);
            iv.setAdjustViewBounds(true);
            iv.setPadding(12,12,12,12);

            // Vérifier si l'image correspond à la cible
            iv.setOnClickListener(v -> {
                boolean correct = o.optString("name").equalsIgnoreCase(target.optString("name"));
                showResultPopup(correct);
            });

            grid.addView(iv);
        }
    }

    /**
     * Joue le son de l'animal cible ou utilise TTS si le son est manquant
     */
    private void playTargetSound() {
        try {
            String soundName = target.optString("sound"); // ex: "lion_roar"
            int resId = getResources().getIdentifier(soundName, "raw", getPackageName());
            if (resId!=0){
                if (mp!=null){ mp.release(); mp=null; }
                mp = MediaPlayer.create(this, resId);
                mp.start();
            } else {
                tts.speak(target.optString("name"));
            }
        } catch (Exception e){ e.printStackTrace(); }
    }

    /**
     * Affiche un pop-up indiquant si la réponse est correcte ou incorrecte
     */
    private void showResultPopup(boolean ok){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(ok ? "Bravo !" : "Essaie encore");
        b.setMessage(ok ? "C'est bien: " + target.optString("name") : "Ce n'est pas ça.");
        b.setPositiveButton("OK",(d,i)->{ if (ok) startRound(); });
        b.show();
        if (ok) sp.playCorrect(); else sp.playWrong();
    }

    /**
     * Récupère l'identifiant d'une image à partir de son nom
     */
    public static int getResId(Context ctx, String name) {
        return ctx.getResources().getIdentifier(name, "drawable", ctx.getPackageName());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mp!=null) mp.release();
        sp.release();
        tts.shutdown();
    }
}
