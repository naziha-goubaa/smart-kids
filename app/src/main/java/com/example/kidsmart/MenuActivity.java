/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité représente le menu principal de l'application éducative pour enfants.
 * Elle affiche une grille de jeux disponibles, chaque élément du menu avec une icône et un titre.
 * Lorsqu'un élément est cliqué, l'animation est jouée puis l'activité correspondante est lancée.
 *
 * Détails :
 * - Utilise un GridLayout pour afficher dynamiquement les éléments du menu.
 * - Chaque élément possède un titre, une icône et un clic listener.
 * - Lance l'activité correspondante à chaque jeu (Memory, Puzzle, Math Quiz, etc.).
 * - Passe des données supplémentaires à certaines activités (ex: MemoryGameActivity avec JSON des animaux).
 * - Fournit une animation de clic pour un retour visuel interactif.
 */

package com.example.kidsmart;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private GridLayout menuContainer; // Conteneur pour les éléments du menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuContainer = findViewById(R.id.menuContainer);

        // Titres des jeux disponibles
        String[] titles = {"Memory", "Puzzle", "Coloriage", "Sons Animaux", "Math Quiz",
                "Drapeaux", "Associer Mot/Image", "Trouve l'intrus",
                "Tracer Lettres", "Quiz Images"};

        // Icônes correspondantes aux jeux
        int[] icons = {R.drawable.memory_icon, R.drawable.puzzle_icon, R.drawable.coloring_icon,
                R.drawable.sound_icon, R.drawable.math_icon, R.drawable.flag_icon,
                R.drawable.match_icon, R.drawable.intruder_icon, R.drawable.write_icon,
                R.drawable.quiz_icon};

        // Activités correspondantes
        Class<?>[] activities = {MemoryGameActivity.class, PuzzleActivity.class, ColoringActivity.class,
                SoundMatchActivity.class, MathQuizActivity.class, FlagColorsActivity.class,
                MatchImageWordActivity.class, IntruderActivity.class, TraceLetterActivity.class, QuizImageActivity.class};

        LayoutInflater inflater = LayoutInflater.from(this);

        // Création dynamique des éléments du menu
        for (int i = 0; i < titles.length; i++) {
            View item = inflater.inflate(R.layout.menu_item, menuContainer, false);
            TextView title = item.findViewById(R.id.menuTitle);
            ImageView icon = item.findViewById(R.id.menuIcon);

            title.setText(titles[i]);
            icon.setImageResource(icons[i]);

            final int index = i;
            item.setOnClickListener(v -> {
                try {
                    // Animation de clic
                    Animator anim = AnimatorInflater.loadAnimator(this, R.animator.menu_click);
                    anim.setTarget(v);
                    anim.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Lancer l'activité après un petit délai pour permettre l'animation
                v.postDelayed(() -> {
                    Intent intent = new Intent(MenuActivity.this, activities[index]);

                    // Passer des données spécifiques si nécessaire
                    if (activities[index] == MemoryGameActivity.class) {
                        String jsonAnimals = "[{\"id\":\"lion\",\"name\":\"Lion\",\"image\":\"img_lion\"}," +
                                "{\"id\":\"cat\",\"name\":\"Chat\",\"image\":\"img_cat\"}," +
                                "{\"id\":\"dog\",\"name\":\"Chien\",\"image\":\"img_dog\"}," +
                                "{\"id\":\"cow\",\"name\":\"Vache\",\"image\":\"img_cow\"}]";
                        intent.putExtra("jsonAnimals", jsonAnimals);
                    }

                    startActivity(intent);
                }, 200);
            });

            menuContainer.addView(item);
        }
    }
}
