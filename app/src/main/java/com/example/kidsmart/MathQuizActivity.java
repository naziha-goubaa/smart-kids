/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité implémente un mini-jeu éducatif de quiz mathématique pour enfants.
 * L'objectif est de résoudre des opérations simples d'addition ou de soustraction
 * (nombres entre 1 et 10) et de choisir la bonne réponse parmi trois options.
 * Le jeu fournit un retour sonore et visuel selon que la réponse est correcte ou non.
 *
 * Détails :
 * - Génère aléatoirement deux nombres et choisit entre addition ou soustraction.
 * - Affiche la question et trois options dont une seule est correcte.
 * - Vérifie la réponse de l’enfant lorsqu’il clique sur un bouton.
 * - Fournit un feedback interactif (son + dialogue).
 * - Passe automatiquement à la question suivante en cas de bonne réponse.
 */

package com.example.kidsmart;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidsmart.utils.SoundPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MathQuizActivity extends AppCompatActivity {

    private TextView tvQ;          // TextView pour afficher la question
    private LinearLayout answers;   // Layout pour contenir les boutons de réponses
    private int correctAns;         // Stocke la réponse correcte
    private SoundPlayer sp;         // Lecteur sonore pour correct/wrong
    private final Random rnd = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

        // Récupération des vues
        tvQ = findViewById(R.id.tvQuestion);
        answers = findViewById(R.id.answers);

        // Initialisation du lecteur sonore
        sp = new SoundPlayer(this);

        // Démarrage de la première question
        next();
    }

    /**
     * Génère une nouvelle question et prépare les options de réponse.
     */
    private void next() {

        // Génération de deux nombres aléatoires
        int a = rnd.nextInt(10) + 1; // 1..10
        int b = rnd.nextInt(10) + 1; // 1..10

        // Choix aléatoire entre addition et soustraction
        boolean add = rnd.nextBoolean();

        if (add) {
            correctAns = a + b;
            tvQ.setText(a + " + " + b + " = ?");
        } else {
            // Pour éviter un résultat négatif
            if (a < b) {
                int t = a;
                a = b;
                b = t;
            }
            correctAns = a - b;
            tvQ.setText(a + " - " + b + " = ?");
        }

        // Nettoyer les anciennes réponses
        answers.removeAllViews();

        // Génération des options sans doublon
        Set<Integer> optsSet = new HashSet<>();
        optsSet.add(correctAns);

        // Ajouter deux mauvaises réponses uniques
        while (optsSet.size() < 3) {
            int fake = correctAns + rnd.nextInt(7) - 3; // variation -3..+3
            if (fake >= 0) optsSet.add(fake);
        }

        List<Integer> opts = new ArrayList<>(optsSet);
        Collections.shuffle(opts); // Mélanger les options

        // Création des boutons pour chaque option
        for (int option : opts) {

            Button btn = new Button(this);
            btn.setText(String.valueOf(option));

            // Paramètres de mise en page avec marges
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(0, 20, 0, 20);
            btn.setLayoutParams(lp);

            // Gestion du clic
            btn.setOnClickListener(v -> checkAnswer(option));

            answers.addView(btn);
        }
    }

    /**
     * Vérifie la réponse choisie et fournit un feedback.
     */
    private void checkAnswer(int chosen) {

        if (chosen == correctAns) {
            sp.playCorrect(); // son correct
            new AlertDialog.Builder(this)
                    .setTitle("Bravo 🎉")
                    .setMessage("Bonne réponse !")
                    .setPositiveButton("Suivant", (d, i) -> next()) // question suivante
                    .setCancelable(false)
                    .show();
        } else {
            sp.playWrong(); // son erreur
            new AlertDialog.Builder(this)
                    .setTitle("Oups ❌")
                    .setMessage("Essaie encore !")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.release(); // Libération du lecteur sonore
    }
}
