package com.example.zw_engineering.ui.addition;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.example.zw_engineering.base.BaseFragment;
import com.example.zw_engineering.base.LoadingPager;
import com.example.zw_engineering.util.MyNetWorkUtils;

import androidx.annotation.Nullable;

public class ReadAdditionFragment extends BaseFragment {
    public static String reportID;
    private ReadHlderView mReadHlderView;
    private String mInfo;
    @Override
    public LoadingPager.LoadedResult initData() {
        if (!TextUtils.isEmpty(reportID)){
            mInfo = MyNetWorkUtils.getInfo(reportID);
        }
        if (TextUtils.isEmpty(mInfo)){
            return LoadingPager.LoadedResult.ERROR;
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }

    @Override
    public View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mReadHlderView = new ReadHlderView(inflater,container);
        mReadHlderView.reportID = reportID;
        if (!TextUtils.isEmpty(mInfo)){
            LogUtils.e(mInfo);
            mReadHlderView.setDataAndRefreshHolderView(mInfo);
        }
        return mReadHlderView.mHolderView;
    }
    class ReadHlderView extends AddHolderView{
        public ReadHlderView(LayoutInflater inflater, @Nullable ViewGroup container) {
            super(inflater, container);
        }

        @Override
        protected void initView(View root) {
            super.initView(root);
            view_date.setClickable(false);  //不可以点击
            mView_calendar.setClickable(false);
            clp_clickable = true;   //不可以点击
        }
    }
}