package com.dome.base.ui.recycler_pull_upanddown;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zw_engineering.R;


/**
 * 创建者：jizhan zhang
 * <p>
 * 描述：自定义的下拉icon
 * <p>
 * 日期：2019-07-09 11:47
 */
public class UpRefreshLayout extends UpRefreshIconView {

    private TextView mTvRefreshStateDescr;
    private ImageView mIvRefreshIcon;
    private ProgressBar mPbProgress;

    public UpRefreshLayout(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_1, this);
        setPadding(30,30,30,30);
        mIvRefreshIcon = findViewById(R.id.iv_refresh_icon);
        mTvRefreshStateDescr = findViewById(R.id.tv_refresh_state_descr);
        mPbProgress = findViewById(R.id.pb_progress);

        mPbProgress.setVisibility(GONE);
        mIvRefreshIcon.setVisibility(VISIBLE);
        mIvRefreshIcon.setImageResource(R.drawable.arrow_up);
        mTvRefreshStateDescr.setText("上拉加载更多");
    }

    @Override
    protected void onPullUp() {
        mPbProgress.setVisibility(GONE);
        mIvRefreshIcon.setVisibility(VISIBLE);
        mIvRefreshIcon.setImageResource(R.drawable.arrow_up);
        mTvRefreshStateDescr.setText("上拉加载更多");
    }

    @Override
    protected void onRefreshing() {
        mPbProgress.setVisibility(VISIBLE);
        mIvRefreshIcon.setVisibility(GONE);
        mTvRefreshStateDescr.setText("加载中...");
    }

    @Override
    protected void onCanRefresh() {
        mPbProgress.setVisibility(GONE);
        mIvRefreshIcon.setVisibility(VISIBLE);
        mIvRefreshIcon.setImageResource(R.drawable.arrow_down);
        mTvRefreshStateDescr.setText("松开加载更多");
    }
}
