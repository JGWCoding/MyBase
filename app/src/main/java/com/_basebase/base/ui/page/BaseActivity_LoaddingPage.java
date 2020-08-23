package com._basebase.base.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.example.zw_engineering.base.LoadingPager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity_LoaddingPage extends AppCompatActivity {
    private LoadingPager mLoadingPager;

    public void triggerLoadData() {
        if (mLoadingPager != null) {
            LogUtils.d("触发加载数据");
            mLoadingPager.triggerLoadData();
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        //返回一个Framgent具体需要展示的View
        if (mLoadingPager == null) {
            mLoadingPager = new LoadingPager(Utils.getApp()) {
                /**
                 * @des 在子线程中具体的加载数据
                 * @call 外界调用loadingPager的triggerLoadData方法的时候
                 * @return
                 */
                @Override
                public LoadedResult initData() {
                    return this.initData();
                }
                /**
                 * @des 决定成功视图是什么
                 * @des 数据和视图如何绑定
                 * @call 触发加载完成, 而且数据加载是成功
                 * @return
                 */
                @Override
                public View initSuccessView() {
                    View view = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
                    initView();
                    return view;
                }
            };
        }
        //触发加载数据-->实际最后,我们的触发加载不应该放到这里 ---> Viewpager里面放fragment会加载
        mLoadingPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLoadingPager.triggerLoadData();
                mLoadingPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //移除自身  ----  android2.3系统有错
        ViewParent parent = mLoadingPager.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mLoadingPager);
        }
        setContentView(mLoadingPager);  //设置了一个容器view(容器提供四种视图)
    }

    protected void initWindow() {

    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();
}