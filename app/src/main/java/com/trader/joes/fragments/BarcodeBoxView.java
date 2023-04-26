package com.trader.joes.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.trader.joes.R;

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

    public void setRect(RectF rect) {
        mRect = rect;
        invalidate();
        requestLayout();
    }
}
