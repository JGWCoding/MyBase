package com._basebase.base.ui.page;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zw_engineering.base.LoadingPager.LoadedResult;

import androidx.annotation.Nullable;

public class DomeBaseFragment extends BaseFragment {
    @Override
    public LoadedResult initData() {    //子线程加载数据
        //开启线程
        String  result_network = "网络返回数据";
        if (TextUtils.isEmpty(result_network)){
            return LoadedResult.EMPTY;
        }
        if(result_network.equals("网络数据返回成功并有需要数据")){
            return LoadedResult.SUCCESS;
        }
        if (result_network.equals("如果中途网络造成异常")){
            return LoadedResult.ERROR;
        }
        if (result_network.equals("如果可以重新反复加载数据")){
//            return LoadedResult.SUCCESS_can_reload;
        }
        return null;
    }

    @Override   //加载数据后绑定成功视图   --->   调用于 initData后
    public View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }
}
