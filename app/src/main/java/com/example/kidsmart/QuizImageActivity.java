/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité implémente un **quiz d’images éducatif pour enfants**.
 * L’objectif est de montrer une image et demander à l’enfant de saisir le mot correspondant.
 * Le jeu fournit un feedback immédiat via un pop-up indiquant si la réponse est correcte ou incorrecte.
 *
 * Détails :
 * - Affiche une image parmi un ensemble pré-défini (pomme, chat, lion, banane).
 * - L’utilisateur doit saisir le mot correspondant à l’image.
 * - Vérifie la réponse en ignorant les majuscules et les espaces.
 * - Affiche un pop-up de succès ou d’erreur.
 * - Génère une nouvelle question après une bonne réponse.
 */

package com.example.kidsmart;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuizImageActivity extends AppCompatActivity {

    private ImageView img;        // Image à deviner
    private EditText answer;      // Champ de saisie de l’utilisateur
    private Button btnCheck;      // Bouton pour valider la réponse

    private List<String> words = Arrays.asList("pomme", "chat", "lion", "banane"); // Liste de mots possibles
    private Map<String, Integer> images = new HashMap<>(); // Association mot → image

    private String currentWord;   // Mot correspondant à l’image affichée

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_image);

        // Récupération des vues
        img = findViewById(R.id.quizImage);
        answer = findViewById(R.id.answerInput);
        btnCheck = findViewById(R.id.submitAnswerBtn);

        // Chargement des images dans la Map
        images.put("pomme", R.drawable.img_apple);
        images.put("chat", R.drawable.img_cat);
        images.put("lion", R.drawable.img_lion);
        images.put("banane", R.drawable.img_banana);

        // Générer la première question
        newQuestion();

        // Vérification de la réponse au clic
        btnCheck.setOnClickListener(v -> checkAnswer());
    }

    /**
     * Génère une nouvelle question en choisissant une image au hasard
     */
    private void newQuestion() {
        currentWord = words.get(new Random().nextInt(words.size()));
        img.setImageResource(images.get(currentWord));
        answer.setText("");
    }

    /**
     * Vérifie si la réponse saisie par l’utilisateur est correcte
     */
    private void checkAnswer() {
        String user = answer.getText().toString().trim().toLowerCase();

        if (user.equals(currentWord)) {
            showPopup(true);   // Réponse correcte
            newQuestion();     // Nouvelle question
        } else {
            showPopup(false);  // Réponse incorrecte
        }
    }

    /**
     * Affiche un pop-up indiquant si la réponse est correcte ou non
     */
    private void showPopup(boolean correct) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(correct ? "Correct !" : "Incorrect !");
        dialog.setIcon(correct ? R.drawable.correct_icon : R.drawable.wrong_icon);
        dialog.setPositiveButton("OK", null);
        dialog.show();
    }
}
