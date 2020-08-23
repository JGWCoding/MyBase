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
import androidx.fragment.app.Fragment;

/**
 * 类    名:  BaseFragment
 */
public abstract class BaseFragment extends Fragment {
    private LoadingPager mLoadingPager;

    public void triggerLoadData() {
        if (mLoadingPager != null) {
            LogUtils.d("触发加载数据");
            mLoadingPager.triggerLoadData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
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
                    return BaseFragment.this.initData();
                }
                /**
                 * @des 决定成功视图是什么
                 * @des 数据和视图如何绑定
                 * @call 触发加载完成, 而且数据加载是成功
                 * @return
                 */
                @Override
                public View initSuccessView() {
                    return BaseFragment.this.initSuccessView(inflater, container, savedInstanceState);
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
        return mLoadingPager;//视图->4种视图中的一种
    }



    /**
     * @return
     * @des 在子线程中加载数据的数据
     * @des 在BaseFramgent中, 不知道initData()方法的具体实现, 交给子类实现
     * @des 子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     * @call 外界调用loadingPager的triggerLoadData方法的时候
     */
    public abstract LoadingPager.LoadedResult initData();   //这个执行在initSuccessView之前

    /**
     * @return
     * @des 决定成功视图是什么
     * @des 数据和视图如何绑定
     * @des 在BaseFragment中, 不知道initSuccessView()方法的具体实现, 交给子类实现
     * @des 子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     * @call 触发加载完成, 而且数据加载是成功
     */
    public abstract View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

//   booblean isFirstLoad;
    //    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        //主要因为viewpager add fragment 会走完onResume方法 所以用这个
//        LogUtils.e(isVisibleToUser,"是否触发了");
//        if (isVisibleToUser && isFirstLoad) {
//            mLoadingPager.triggerLoadData();
//            isFirstLoad = false;
//        }
//    }
}
