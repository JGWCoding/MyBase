package com.example.zw_engineering.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * 类    名:  BaseHolder
 * 创 建 者:  伍碧林
 * 创建时间:  2016/12/20 16:54
 * 描    述：1.提供视图
 * 描    述：2.接收数据
 * 描    述：3.数据和视图的绑定
 */
public abstract class BaseHolder<HOLDERBEANTYPE> {

    public View mHolderView;//view
    private final SparseArray<View> views = new SparseArray<>();

    public BaseHolder() {
        mHolderView = initHolderView();
        //找出所能提供视图里面的孩子
//        ButterKnife.bind(this, mHolderView);
        initView(mHolderView);
        //mHolderView<找一个符合ViewHolder条件的类的对象>,绑定在自己身上
        mHolderView.setTag(this);
    }

    public BaseHolder(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHolderView = initHolderView(inflater, container, savedInstanceState);
        initView(mHolderView);
        mHolderView.setTag(this);
    }

    protected View initHolderView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    ;


    /**
     * @des 接收数据
     * @des 数据和视图的绑定
     */
    public void setDataAndRefreshHolderView(HOLDERBEANTYPE data) {
        refreshHolderView(data);
    }

    /**
     * @return
     * @des 初始化BaseHolder所能提供的视图
     * @des 在BaseHolder中不知道initHolderView()方法的具体实现, 交给子类
     * @des 子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     * @call BaseHolder一旦被创建的时候
     */
    public abstract View initHolderView();

    protected abstract void initView(View root); //初始化view

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mHolderView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public BaseHolder setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseHolder setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public void setText(TextView view, String text) {
        if (view != null && !TextUtils.isEmpty(text)) {
            view.setText(text);
        }
    }

    /**
     * @param data
     * @des 数据和视图的绑定
     * @des 在BaseHolder中, 不知道refreshHolderView()方法的具体实现, 交给子类
     * @des 子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     */
    public abstract void refreshHolderView(HOLDERBEANTYPE data);
}
