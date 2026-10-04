/**
 * Fonctionnalité principale :
 * --------------------------
 * Cette activité implémente un jeu éducatif de type "Puzzle" pour enfants.
 * L'objectif est de reconstituer une image découpée en plusieurs pièces (ici un puzzle 2x2).
 *
 * Détails :
 * - Charge l'image complète depuis les ressources et l'affiche en petit aperçu pour aide.
 * - Découpe l'image en 4 pièces (2x2) et les place dans une grille.
 * - Mélange les pièces aléatoirement.
 * - Permet de réorganiser les pièces via glisser-déposer (drag & drop).
 * - Vérifie si le puzzle est correctement reconstitué et affiche un pop-up de réussite.
 */

package com.example.kidsmart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuzzleActivity extends AppCompatActivity {

    private GridLayout grid;               // Grille contenant les pièces du puzzle
    private ImageView originalImage;       // Image originale pour référence

    private final int rows = 2, cols = 2;  // Puzzle 2x2
    private final List<Bitmap> originalTiles = new ArrayList<>(); // Stocke les pièces originales

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_puzzle);

        grid = findViewById(R.id.puzzleGrid);
        originalImage = findViewById(R.id.originalImage);
        Button btnCheck = findViewById(R.id.btnCheckPuzzle);

        grid.setColumnCount(cols);

        // Construire le puzzle après que la grille soit mesurée
        grid.post(this::buildPuzzle);

        // Bouton pour vérifier si le puzzle est résolu
        btnCheck.setOnClickListener(v -> checkSolved());
    }

    /**
     * Crée le puzzle : découpe, affiche et mélange les pièces.
     */
    private void buildPuzzle() {

        // Charger l'image complète
        Bitmap fullImage = BitmapFactory.decodeResource(
                getResources(),
                getResources().getIdentifier("puzzle_image", "drawable", getPackageName())
        );

        // Afficher l'image originale pour aide
        originalImage.setImageBitmap(fullImage);

        // Redimensionner l'image pour qu'elle rentre dans la grille
        int gridSize = Math.min(grid.getWidth(), grid.getHeight());
        fullImage = Bitmap.createScaledBitmap(fullImage, gridSize, gridSize, true);

        int tileW = fullImage.getWidth() / cols;
        int tileH = fullImage.getHeight() / rows;

        originalTiles.clear();
        grid.removeAllViews();

        // Découper l'image en pièces
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                Bitmap tile = Bitmap.createBitmap(fullImage, c * tileW, r * tileH, tileW, tileH);
                originalTiles.add(tile);

                ImageView iv = new ImageView(this);
                iv.setImageBitmap(tile);
                iv.setTag(originalTiles.size() - 1);

                iv.setAdjustViewBounds(true);
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iv.setPadding(4, 4, 4, 4);

                // Permettre drag & drop
                iv.setOnLongClickListener(v -> {
                    View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(null, shadow, v, 0);
                    return true;
                });

                iv.setOnDragListener(dragListener);
                grid.addView(iv);
            }
        }

        shuffleGridImages(); // Mélanger les pièces
    }

    /**
     * Mélange les pièces du puzzle dans la grille
     */
    private void shuffleGridImages() {
        List<Bitmap> shuffled = new ArrayList<>(originalTiles);
        Collections.shuffle(shuffled);

        for (int i = 0; i < grid.getChildCount(); i++) {
            ImageView iv = (ImageView) grid.getChildAt(i);
            iv.setImageBitmap(shuffled.get(i));
        }
    }

    /**
     * Drag & Drop listener pour échanger les pièces
     */
    private final View.OnDragListener dragListener = (v, e) -> {
        if (e.getAction() == DragEvent.ACTION_DROP) {

            ImageView from = (ImageView) e.getLocalState();
            ImageView to = (ImageView) v;

            Drawable temp = from.getDrawable();
            from.setImageDrawable(to.getDrawable());
            to.setImageDrawable(temp);
        }
        return true;
    };

    /**
     * Vérifie si le puzzle est correctement reconstitué
     */
    private void checkSolved() {

        for (int i = 0; i < grid.getChildCount(); i++) {
            ImageView iv = (ImageView) grid.getChildAt(i);
            Bitmap current = ((BitmapDrawable) iv.getDrawable()).getBitmap();

            if (!current.sameAs(originalTiles.get(i))) {
                Toast.makeText(this, "❌ Ce n’est pas encore correct !", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Afficher le pop-up de réussite
        androidx.appcompat.app.AlertDialog.Builder dialog =
                new androidx.appcompat.app.AlertDialog.Builder(this);

        dialog.setTitle("Bravo !");
        dialog.setMessage("Tu as complété le puzzle avec succès !");
        dialog.setIcon(getResources().getIdentifier("correct_icon", "drawable", getPackageName()));
        dialog.setPositiveButton("OK", (d, w) -> d.dismiss());
        dialog.show();
    }
}
