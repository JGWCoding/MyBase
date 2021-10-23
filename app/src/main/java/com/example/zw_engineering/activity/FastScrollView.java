package com.example.zw_engineering.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ScrollView;

public class FastScrollView extends ScrollView {

    private int mScrollDiff = 100;
    private int mTopOffset = 100;
    private int mBottomOffset = 1080 - 100;

    public FastScrollView(Context context) {
        this(context,null);
    }

    public FastScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setVerticalScrollBarEnabled(false);
        setClipChildren(false);
        setClipToPadding(false);
    }

    public void setEdgeDiff(int topScreenOffset, int bottomScreenOffset, int scrollDiff,
                            int windowHeight) {
        this.mScrollDiff = scrollDiff;
        this.mTopOffset = topScreenOffset;
        this.mBottomOffset = windowHeight - bottomScreenOffset;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /*View focus = findFocus();
        if (focus!=null){
            int[] locations = { 0, 0 };
            focus.getLocationInWindow(locations);
            if (locations[1]+focus.getHeight() > mBottomOffset){
                smoothScrollBy(0, locations[1]+focus.getHeight()-mBottomOffset);
            }else if (locations[1] < mTopOffset){
                scrollTo(0, -mTopOffset+locations[1]);
            }
        }*/
    }

    public void scrollToView(View nextView) {
        int[] locations = {0, 0};
        nextView.getLocationInWindow(locations);
        Log.d("scrollToView", "location:" + locations[0] + "," + locations[1]);
        Log.d("scrollToView", "compare:" + mTopOffset + "," + mBottomOffset + "," + nextView.getHeight());
        View focus = findFocus();
        if (locations[1] < mTopOffset) // 100
        {
            Log.d("scrollToView", "UP------>:" + (locations[1] - mTopOffset));
            if (focus == null) {
                scrollTo(0, locations[1] - mTopOffset);
            } else {
                smoothScrollBy(0, locations[1] - mTopOffset);
            }
        } else if (locations[1] > (mBottomOffset - nextView.getHeight())) {
            Log.d("scrollToView", "DOWN------>:" + (locations[1] - (mBottomOffset - nextView.getHeight())));
            if (focus == null) {
                scrollTo(0,
                        locations[1] - (mBottomOffset - nextView.getHeight()));
            } else {
                smoothScrollBy(0,
                        locations[1] - (mBottomOffset - nextView.getHeight()));
            }
        }
        nextView.requestFocus();
    }

    public void onFocusInit() {
        View focus = findFocus();
        if (focus != null) {
            int[] locations = {0, 0};
            focus.getLocationInWindow(locations);
            if (locations[1] + focus.getHeight() > mBottomOffset) {
                smoothScrollBy(0, locations[1] + focus.getHeight() - mBottomOffset);
            } else if (locations[1] < mTopOffset) {
                scrollTo(0, -mTopOffset + locations[1]);
            }
        }


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            View focusView = this.findFocus();
            Log.d("tt", "cur focus view->" + focusView);
            if (focusView != null) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    int direction = event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP ? View.FOCUS_UP
                            : View.FOCUS_DOWN;
                    View nextView = FocusFinder.getInstance().findNextFocus(this, focusView,
                            direction);
                    Log.d("tt", "next focus view->" + nextView);
                    if (nextView == null) {
                        smoothScrollBy(0, direction == View.FOCUS_UP ? -mScrollDiff : mScrollDiff);
                        return super.dispatchKeyEvent(event);
                    } else {
                        int[] locations = {0, 0};
                        nextView.getLocationInWindow(locations);
                        Log.d("tt", "location:" + locations[0] + "," + locations[1]);
                        Log.d("tt", "compare:" + mTopOffset + "," + mBottomOffset + "," + nextView.getHeight());
                        if (locations[1] < mTopOffset) // 100
                        {
                            Log.d("tt", "UP------>:" + (locations[1] - mTopOffset));
                            smoothScrollBy(0, locations[1] - mTopOffset);
                            nextView.requestFocus();
                            return true;
                        } else if (locations[1] > (mBottomOffset - nextView.getHeight())) {
                            Log.d("tt", "DOWN------>:" + (locations[1] - (mBottomOffset - nextView.getHeight())));
                            smoothScrollBy(0,
                                    locations[1] - (mBottomOffset - nextView.getHeight()));
                            nextView.requestFocus();
                            return true;
                        }
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}