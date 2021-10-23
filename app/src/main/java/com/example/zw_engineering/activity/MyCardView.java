package com.example.zw_engineering.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class MyCardView extends CardView {
    public MyCardView(@NonNull @NotNull Context context) {
        super(context);
    }

    public MyCardView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCardView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        float radius = getRadius();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        RectF rect = new RectF(0,0,radius*2,radius*2);
        canvas.drawArc(rect,180,90,false,paint);
        rect = new RectF(width-2*radius,0,width,radius*2);
        canvas.drawArc(rect,-90,90,false,paint);
        rect = new RectF(0,height-2*radius,radius*2,height);
        canvas.drawArc(rect,90,90,false,paint);
        rect = new RectF(width-2*radius,height-2*radius,width,height);
        canvas.drawArc(rect,0,90,false,paint);

        canvas.drawLine(radius,0,getMeasuredWidth()-radius,0,paint);
        canvas.drawLine(radius,getMeasuredHeight(),getMeasuredWidth()-radius,getMeasuredHeight(),paint);
        canvas.drawLine(0,radius,0,getMeasuredHeight()-radius,paint);
        canvas.drawLine(getMeasuredWidth(),radius,getMeasuredWidth(),getMeasuredHeight()-radius,paint);
    }
}
