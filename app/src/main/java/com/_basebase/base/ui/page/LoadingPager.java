package com._basebase.base.ui.page;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;
import com.example.zw_engineering.R;

/**
 * 类    名:  LoadingPager
 * 描    述：1.提供视图(加载中视图,加载失败视图,空视图,成功视图)
 * 描    述：2.加载数据
 * 描    述：3.数据和视图的绑定
 * 描    述：静态视图的创建,以及视图的切换
 * 描    述：数据触发加载流程-
 */
public abstract class LoadingPager extends FrameLayout {
    public static final int STATE_LOADING = 0;//加载中
    public static final int STATE_EMPTY = 1;//空
    public static final int STATE_ERROR = 2;//错误
    public static final int STATE_SUCCESS = 3;//成功
    public static final int STATE_SUCCESS_Can_reload = 4;//成功并可以重新触发加载(应用于页面可以重新加载数据)
    private int mCurState = STATE_LOADING;//当前状态,默认是加载中
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;
    private LoadDataTask mLoadDataTask;

    public LoadingPager(Context context) {
        super(context);
        //先创建3个静态视图(加载中视图,加载失败视图,空视图)
        initCommontView();
    }

    /**
     * @des 初始化常规视图(3个静态视图)
     */
    private void initCommontView() {
        //加载中视图
        mLoadingView = View.inflate(Utils.getApp(), R.layout.pager_loading, null);
        addView(mLoadingView);
        //加载失败视图
        mErrorView = View.inflate(Utils.getApp(), R.layout.pager_error, null);
        addView(mErrorView);

        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //触发重新加载
                triggerLoadData();
            }
        });

        //空视图
        mEmptyView = View.inflate(Utils.getApp(), R.layout.pager_empty, null);
        addView(mEmptyView);
        //同一时刻,只能对外提供一个视图
        refreshUIByState();
    }

    /**
     * @des 根据当前的状态展示不同的ui
     * @call 1.LoadingPager初始化创建的时候
     * @call 2.外界调用了trggerLoadData(), 数据加载之前
     * @call 3.外界调用了trggerLoadData(), 数据加载完成
     */
    private void refreshUIByState() {
        //隐藏所有的视图
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        if (mSuccessView != null) {
            mSuccessView.setVisibility(View.GONE);
        }
        switch (mCurState) {
            case STATE_LOADING://对外提供 加载中视图
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case STATE_EMPTY://对外提供 空视图
                mEmptyView.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR://对外提供错误视图
                mErrorView.setVisibility(View.VISIBLE);
                break;
            case STATE_SUCCESS://对外提供 成功视图
                if (mSuccessView == null) {
                    //创建成功视图-->直接给用户看的视图
                    mSuccessView = initSuccessView();
                    //加入容器
                    addView(mSuccessView);
                }
                //显示成功视图
                mSuccessView.setVisibility(View.VISIBLE);
                break;
            case STATE_SUCCESS_Can_reload://对外提供 成功视图
                if (mSuccessView == null) {
                    //创建成功视图-->直接给用户看的视图
                    mSuccessView = initSuccessView();
                    //加入容器
                    addView(mSuccessView);
                }
                //显示成功视图
                mSuccessView.setVisibility(View.VISIBLE);
                break;
            default:
                LogUtils.e("没有匹配视图,mCurState==null");
                break;
        }
    }


    /**
     * @des 触发加载数据
     * @call 外界需要LoadingPager触发加载数据的时候调用
     */
    public void triggerLoadData() {
        if (mCurState != STATE_SUCCESS && mLoadDataTask == null) {//当前状态不是成功才加载
//            LogUtils.sf("triggerLoadData");
            //异步加载前,重置状态
            mCurState = STATE_LOADING;
            refreshUIByState();
            //异步加载
            mLoadDataTask = new LoadDataTask();
//            new Thread(mLoadDataTask).start();
            ThreadUtils.executeByCached(mLoadDataTask);
//            ThreadPoolProxyFactory.getNormalThreadPoolProxy().submit(mLoadDataTask);
        }
        if (mCurState == STATE_SUCCESS_Can_reload && mLoadDataTask == null) {//当前状态是成功后可以重新加载
            mCurState = STATE_LOADING;
            refreshUIByState();
            //异步加载
            mLoadDataTask = new LoadDataTask();
            ThreadUtils.executeByCached(mLoadDataTask);
        }
    }

    class LoadDataTask extends ThreadUtils.SimpleTask<Integer> {
        @Override
        public Integer doInBackground() throws Throwable {
            //在子线程中,得到具体数据
            LoadedResult loadedResult = initData();
            Integer resState = loadedResult.getState();
            mLoadDataTask = null;
            return resState;
        }

        @Override
        public void onSuccess(Integer result) {
            mCurState = result;
            refreshUIByState();
        }

        @Override
        public void onFail(Throwable t) {
            mCurState = STATE_ERROR;
            refreshUIByState();
            LogUtils.e(t.getMessage());
        }
    }

//    class LoadDataTask implements Runnable {
//        @Override
//        public void run() {
//            //在子线程中,得到具体数据
//            LoadedResult loadedResult = initData();
//            int resState = loadedResult.getState();
//            //处理数据
//            mCurState = resState;
//            Utils.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //刷新ui-->mCurState-->refreshUIByState()方法
//                    refreshUIByState();
//                }
//            });
//            //走到run方法体的最后相当于任务执行完成了.置空任务
//            mLoadDataTask = null;
//        }
//    }

    /**
     * @return
     * @des 在子线程中加载具体的数据
     * @des 在LoadingPager, 不知道initData()具体实现, 交给子类实现
     * @des 子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     * @call 外界调用loadingPager的triggerLoadData方法的时候
     */
    public abstract LoadedResult initData();

    /**
     * @return
     * @des 决定成功视图是啥
     * @des 以及数据和视图怎么绑定(以为用户想看到的就是最后绑定之后的视图)
     * @des 在LoadingPager中, 不知道initSuccessView()方法的具体实现, 交给子类
     * @des 子类是必须实现定义成为抽象方法, 交给子类具体实现
     * @call 触发加载完成, 而且数据加载是成功
     */
    public abstract View initSuccessView();

    //定义枚举,控制返回类型
    public enum LoadedResult {
        SUCCESS(STATE_SUCCESS), EMPTY(STATE_EMPTY), ERROR(STATE_ERROR), SUCCESS_can_reload(STATE_SUCCESS_Can_reload);

        public int state;

        LoadedResult(int state) {
            this.state = state;
        }


        public int getState() {
            return state;
        }
    }
}