/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité permet aux enfants de **s’exercer au tracé des lettres**.
 * L’enfant trace une lettre à l’écran en suivant le contour affiché.
 * Le jeu fournit un **feedback immédiat** sur la précision du tracé.
 *
 * Détails :
 * - Affiche une lettre à tracer via un drawable (ex: "letter_a_outline").
 * - L’enfant trace avec le doigt sur la zone de dessin (TraceView).
 * - En cliquant sur "Vérifier", l’application calcule le pourcentage de couverture du tracé.
 * - Affiche un pop-up avec le score et un feedback visuel (icône étoile pour ≥80%, alerte sinon).
 * - La lettre peut être facilement changée via la méthode setCurrentLetter().
 */

package com.example.kidsmart;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TraceLetterActivity extends AppCompatActivity {

    private TraceView traceView;   // Zone où l’enfant trace la lettre

    private String currentLetter = "a";  // Lettre actuelle à tracer (modifiable)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_letter);

        traceView = findViewById(R.id.traceView);


        // Charger le contour de la lettre actuelle
        int drawableId = getResources().getIdentifier(
                "letter_" + currentLetter + "_outline",
                "drawable",
                getPackageName()
        );

        if (drawableId != 0) {
            traceView.setLetterDrawable(drawableId);
        } else {
            Toast.makeText(this, "Le dessin de la lettre n'existe pas", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Permet de changer la lettre à tracer
     */
    public void setCurrentLetter(String letter) {
        currentLetter = letter.toLowerCase();
        int drawableId = getResources().getIdentifier(
                "letter_" + currentLetter + "_outline",
                "drawable",
                getPackageName()
        );
        if (drawableId != 0) {
            traceView.setLetterDrawable(drawableId);
            traceView.resetTrace();
        } else {
            Toast.makeText(this, "Lettre introuvable: " + letter, Toast.LENGTH_SHORT).show();
        }
    }
}
