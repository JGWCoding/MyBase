package com.dome.base.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * 阴影渐模糊效果
 */
class ShadowTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint mPaint;

    public ShadowTextView(Context context) {
        super(context);
        initPaint();
    }


    public ShadowTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ShadowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradient linearGradient = new LinearGradient(0, 0, 0, getMeasuredHeight(),
                new int[]{Color.TRANSPARENT,Color.WHITE},new float[]{0.0f,1}, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        Rect rect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(rect,mPaint);
    }
}
