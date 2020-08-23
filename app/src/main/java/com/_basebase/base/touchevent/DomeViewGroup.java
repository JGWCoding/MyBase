package com._basebase.base.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;

public class DomeViewGroup extends LinearLayout {
    public DomeViewGroup(Context context) {
        super(context);
        setClickable(true);
        addView(new DomeView(context));
    }

    public DomeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DomeViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.e("DomeViewGroup dispatchTouchEvent",ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtils.e("DomeViewGroup onInterceptTouchEvent",ev.getAction());
//        requestDisallowInterceptTouchEvent(false);   //
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.e("DomeViewGroup onTouchEvent",event.getAction());
        return super.onTouchEvent(event);
    }
}
