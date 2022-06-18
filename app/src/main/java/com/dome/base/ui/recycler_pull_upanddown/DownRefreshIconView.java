package com.dome.base.ui.recycler_pull_upanddown;

import android.content.Context;
import android.widget.LinearLayout;

public abstract class DownRefreshIconView extends LinearLayout {

    public static final int TYPE_DOWN = 0;
    public static final int TYPE_CAN_REFRESH = 1;
    public static final int TYPE_REFRESHING = 2;

    private int mType = TYPE_DOWN;

    public DownRefreshIconView(Context context) {
        super(context);

        initView();
    }

    protected abstract void initView();

    public void setType(int type) {

        if (type == this.mType) {
            return;
        }

        switch (type) {
            case TYPE_CAN_REFRESH:
                onCanRefresh();
                break;
            case TYPE_REFRESHING:
                onRefreshing();
                break;
            case TYPE_DOWN:
            default:
                onPullDown();
        }
        this.mType = type;
    }

    /**
     * 正在下拉的回调
     */
    protected abstract void onPullDown();

    /**
     * 正在刷新的回调
     */
    protected abstract void onRefreshing();

    /**
     * 能够进行刷新时的回调
     */
    protected abstract void onCanRefresh();

}