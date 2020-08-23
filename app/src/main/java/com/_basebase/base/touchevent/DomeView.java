package com._basebase.base.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import androidx.annotation.Nullable;

public class DomeView extends View {
    public DomeView(Context context) {
        super(context);
        setClickable(true);
    }

    public DomeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DomeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.e("View dispatchTouchEvent",ev.getAction());
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        getParent().requestDisallowInterceptTouchEvent(true);
        LogUtils.e("View onTouchEvent",event.getAction());
//        return super.onTouchEvent(event);
        return false;
    }
}
