package com.example.kidsmart.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * SoundPlayer
 * -----------
 * Cette classe gère la lecture des sons du jeu.
 * Elle charge 3 sons depuis /res/raw :
 *    - correct.wav  → bonne réponse
 *    - wrong.wav    → mauvaise réponse
 *    - win.wav      → victoire
 *
 * Elle fournit des méthodes simples : playCorrect(), playWrong(), playWin().
 * Elle libère également les MediaPlayer pour éviter les fuites mémoire.
 */
public class SoundPlayer {

    private MediaPlayer mpCorrect, mpWrong, mpWin;

    /**
     * Constructeur : charge les fichiers audio "correct", "wrong" et "win"
     * situés dans le dossier /res/raw.
     */
    public SoundPlayer(Context ctx) {
        try {
            // Charge le son de bonne réponse
            mpCorrect = MediaPlayer.create(
                    ctx,
                    ctx.getResources().getIdentifier("correct", "raw", ctx.getPackageName())
            );
        } catch (Exception e) { e.printStackTrace(); }

        try {
            // Charge le son de mauvaise réponse
            mpWrong = MediaPlayer.create(
                    ctx,
                    ctx.getResources().getIdentifier("wrong", "raw", ctx.getPackageName())
            );
        } catch (Exception e) { e.printStackTrace(); }

        try {
            // Charge le son de victoire
            mpWin = MediaPlayer.create(
                    ctx,
                    ctx.getResources().getIdentifier("win", "raw", ctx.getPackageName())
            );
        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Joue le son de bonne réponse */
    public void playCorrect() {
        if (mpCorrect != null) mpCorrect.start();
    }

    /** Joue le son de mauvaise réponse */
    public void playWrong() {
        if (mpWrong != null) mpWrong.start();
    }

    /** Joue le son de victoire */
    public void playWin() {
        if (mpWin != null) mpWin.start();
    }

    /**
     * Libère tous les MediaPlayer pour éviter les fuites mémoire.
     * À appeler dans onDestroy() ou à la fin du jeu.
     */
    public void release() {
        if (mpCorrect != null) {
            mpCorrect.release();
            mpCorrect = null;
        }

        if (mpWrong != null) {
            mpWrong.release();
            mpWrong = null;
        }

        if (mpWin != null) {
            mpWin.release();
            mpWin = null;
        }
    }
}
