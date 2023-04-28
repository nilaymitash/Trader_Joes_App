package com.trader.joes.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.trader.joes.R;

/**
 * This view adds a red rectangle on the screen
 * Intended to be used by the barcode analyzer,
 * to add a red rectangle on the screen when a barcode is found.
 */
public class BarcodeBoxView extends View {

    private Paint paint = new Paint();
    private RectF mRect = new RectF();

    public BarcodeBoxView(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cornerRadius = 10f;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.red));
        paint.setStrokeWidth(5f);

        if(canvas != null) {
            canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
        }
    }

    /**
     * This function is used to set and reset rect on a view
     * @param rect
     */
    public void setRect(RectF rect) {
        mRect = rect;
        invalidate();
        requestLayout();
    }
}
