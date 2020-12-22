package com._basebase.base.ui.recycler_pull_upanddown;

import android.content.Context;
import android.widget.LinearLayout;

public abstract class UpRefreshIconView extends LinearLayout {

    public static final int TYPE_UP = 0;
    public static final int TYPE_CAN_REFRESH = 1;
    public static final int TYPE_REFRESHING = 2;

    private int mType = TYPE_UP;

    public UpRefreshIconView(Context context) {
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
            case TYPE_UP:
            default:
                onPullUp();
        }
        this.mType = type;
    }

    protected abstract void onPullUp();

    protected abstract void onRefreshing();

    protected abstract void onCanRefresh();


}