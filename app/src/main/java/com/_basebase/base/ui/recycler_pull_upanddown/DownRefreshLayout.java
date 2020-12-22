package com._basebase.base.ui.recycler_pull_upanddown;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zw_engineering.R;

public class DownRefreshLayout extends DownRefreshIconView {

    private TextView mTvRefreshStateDescr;

    public DownRefreshLayout(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        setOrientation(VERTICAL);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_0, this);
        ImageView ivRefreshIcon = findViewById(R.id.iv_refresh_icon);
        mTvRefreshStateDescr = findViewById(R.id.tv_refresh_state_descr);
        //加载GIF动画
        ivRefreshIcon.setImageResource(R.drawable.pull_refreshing_loading);
//        ImageLoader.getInstance().loadImage(this.getContext(), ImageConfig.builder()
//                .source(R.raw.pull_down_refresh_icon)
//                .imageView(ivRefreshIcon).build()
//        );
        mTvRefreshStateDescr.setText("下拉刷新");
    }

    @Override
    protected void onPullDown() {
        mTvRefreshStateDescr.setText("下拉刷新");
    }

    @Override
    protected void onRefreshing() {
        mTvRefreshStateDescr.setText("刷新中...");
    }

    @Override
    protected void onCanRefresh() {
        mTvRefreshStateDescr.setText("松开刷新");
    }
}