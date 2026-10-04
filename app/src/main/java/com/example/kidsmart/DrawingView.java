package com.example.kidsmart;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * DrawingView
 * -----------
 * Vue personnalisée utilisée pour dessiner avec le doigt dans le jeu de coloriage.
 * Fonctionnalités :
 *   - Dessin fluide avec Path (pinceau)
 *   - Bitmap + Canvas pour conserver les traits
 *   - Possibilité de changer la couleur du pinceau
 *   - Effacer le dessin
 *   - Charger une image de base (outline) à colorier
 */
public class DrawingView extends View {

    private Bitmap mBitmap;       // Surface de dessin
    private Canvas mCanvas;       // Canvas associé au Bitmap
    private Path mPath;           // Chemin actuel tracé par le doigt
    private Paint mBitmapPaint;   // Paint utilisé pour dessiner le Bitmap
    private Paint mPaint;         // Paint du pinceau
    private int paintColor = Color.RED;  // Couleur par défaut du pinceau

    /**
     * Constructeur : initialise les pinceaux et les éléments de dessin.
     */
    public DrawingView(Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);

        mPath = new Path();

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        // Configuration du pinceau
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(paintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);  // Épaisseur du trait
    }

    /**
     * Change la couleur du pinceau
     */
    public void setPaintColor(int color) {
        paintColor = color;
        mPaint.setColor(color);
    }

    /**
     * Efface tout le dessin en vidant le Bitmap.
     */
    public void clear() {
        if (mBitmap != null) {
            mBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
        }
    }

    /**
     * Appelé quand la taille de la vue change (création du Bitmap).
     * Crée la surface de dessin et charge l'image outline si disponible.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Initialisation du bitmap une seule fois
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

            // Charge une image “outline” depuis drawable (ex: outline_image.png)
            int outlineId = getResources().getIdentifier(
                    "outline_image", "drawable", getContext().getPackageName()
            );

            if (outlineId != 0) {
                Bitmap outline = BitmapFactory.decodeResource(getResources(), outlineId);

                // Mise à l’échelle pour remplir la zone de dessin
                Bitmap scaled = Bitmap.createScaledBitmap(outline, w, h, true);

                // Dessine l'outline dans le Bitmap
                mCanvas.drawBitmap(scaled, 0, 0, mBitmapPaint);
            }
        }
    }

    /**
     * Dessine :
     *  - le contenu du Bitmap (ancien dessin)
     *  - le trait en cours (Path)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    // Position précédente (pour lisser le tracé)
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    /** Début du tracé */
    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    /** Mouvement du doigt : lissage avec quadTo */
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    /** Fin du tracé : commit sur le Bitmap */
    private void touchUp() {
        mPath.lineTo(mX, mY);

        // Dessine le Path permanent dans le Bitmap
        mCanvas.drawPath(mPath, mPaint);

        // Réinitialise le Path pour le prochain trait
        mPath.reset();
    }

    /**
     * Gestion du toucher (dessin au doigt)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate(); // redessiner
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }
}
