package com.example.kidsmart;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class TraceView extends View {
    private Bitmap baseBmp, drawBmp;
    private Canvas drawCanvas;
    private Paint drawPaint, basePaint;
    private Path path;
    private int targetResId;

    public TraceView(Context c, @Nullable AttributeSet a){
        super(c,a);
        path = new Path();
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setColor(Color.BLUE);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(30);
        basePaint = new Paint();
    }

    public void setLetterDrawable(int resId){
        targetResId = resId;
        if (getWidth() > 0) initBitmaps();
    }

    private void initBitmaps(){
        baseBmp = BitmapFactory.decodeResource(getResources(), targetResId);
        baseBmp = Bitmap.createScaledBitmap(baseBmp, getWidth(), getHeight(), true);
        drawBmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(drawBmp);
        invalidate();
    }

    @Override protected void onSizeChanged(int w,int h,int ow,int oh){
        super.onSizeChanged(w,h,ow,oh);
        if (targetResId!=0) initBitmaps();
    }

    @Override protected void onDraw(Canvas c){
        if (baseBmp!=null) c.drawBitmap(baseBmp,0,0,basePaint);
        c.drawBitmap(drawBmp,0,0,null);
        c.drawPath(path, drawPaint);
    }

    private float mX, mY;
    private void touchStart(float x,float y){ path.moveTo(x,y); mX=x; mY=y; }
    private void touchMove(float x,float y){ path.quadTo(mX,mY,(x+mX)/2,(y+mY)/2); mX=x; mY=y; }
    private void touchUp(){ drawCanvas.drawPath(path, drawPaint); path.reset(); }

    @Override public boolean onTouchEvent(MotionEvent e){
        float x = e.getX(), y = e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN: touchStart(x,y); invalidate(); break;
            case MotionEvent.ACTION_MOVE: touchMove(x,y); invalidate(); break;
            case MotionEvent.ACTION_UP: touchUp(); invalidate(); break;
        }
        return true;
    }

    // evaluation: calculate percentage of base stroke area covered by drawing (approx)
    public float evaluateCoverage(){
        if (baseBmp==null || drawBmp==null) return 0f;
        int w = baseBmp.getWidth(), h = baseBmp.getHeight();
        int baseCount=0, hitCount=0;
        for (int x=0;x<w;x+=8){
            for (int y=0;y<h;y+=8){
                int p = baseBmp.getPixel(x,y);
                // assume letter stroke has non-white pixel
                if (Color.red(p)<250 || Color.green(p)<250 || Color.blue(p)<250){
                    baseCount++;
                    int dp = drawBmp.getPixel(x,y);
                    if (Color.red(dp)!=0 || Color.green(dp)!=0 || Color.blue(dp)!=0) hitCount++;
                }
            }
        }
        return baseCount==0?0f: (hitCount*100f/baseCount);
    }
    public void resetTrace(){
        if(drawBmp != null && drawCanvas != null){
            drawBmp.eraseColor(Color.TRANSPARENT); // vide le bitmap
            path.reset(); // réinitialise le Path
            invalidate(); // redessine la vue
        }
    }
}
