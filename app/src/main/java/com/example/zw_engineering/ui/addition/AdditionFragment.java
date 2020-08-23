package com.example.zw_engineering.ui.addition;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zw_engineering.base.BaseFragment;
import com.example.zw_engineering.base.LoadingPager;
import com.example.zw_engineering.util.MyNetWorkUtils;

import androidx.annotation.Nullable;

public class AdditionFragment extends BaseFragment {
    public static String reportID;
    private AddHolderView mAddHolderView;
    private String mInfo;

    @Override
    public LoadingPager.LoadedResult initData() {
        if (!TextUtils.isEmpty(reportID)){
            mInfo = MyNetWorkUtils.getInfo(reportID);   //拿到数据
        }else{
            return LoadingPager.LoadedResult.SUCCESS;
        }

        if (TextUtils.isEmpty(mInfo)){
            return LoadingPager.LoadedResult.ERROR;
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }

    @Override
    public View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAddHolderView = new AddHolderView(inflater,container);
        if (!TextUtils.isEmpty(mInfo)){
           mAddHolderView.setDataAndRefreshHolderView(mInfo);
        }
        mAddHolderView.reportID = null; //置空,为添加数据
        return mAddHolderView.mHolderView;
    }
}