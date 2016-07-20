package com.wenym.grooo.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.wenym.grooo.util.Toasts;
import com.wenym.grooo.util.stackblur.StackBlurManager;


/**
 * Created by runzii on 15-11-3.
 */
public class AutoBlurFrameLayout extends View {

    public AutoBlurFrameLayout(Context context) {
        super(context);
    }

    public AutoBlurFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoBlurFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float radius = 2;
        float scaleFactor = 8;

        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()/scaleFactor), (int)(view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = new StackBlurManager(overlay).processNatively((int)radius);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        Toasts.show("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }
}
