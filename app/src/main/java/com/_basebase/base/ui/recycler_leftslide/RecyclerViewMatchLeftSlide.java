package com._basebase.base.ui.recycler_leftslide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMatchLeftSlide extends RecyclerView {
    public RecyclerViewMatchLeftSlide(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerViewMatchLeftSlide(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewMatchLeftSlide(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context)); //默认设置LinearLayoutManager
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //RecyclerView本身有onInterceptTouchEvent方法，有一些处理，
        //不写上这个调用，还不行
        super.onInterceptTouchEvent(e);
        LogUtils.e(e.getAction());
        int action = e.getAction();
        //确保down事件能传递下去 ----  能让里面itemView先处理onTouchEvent事件(决定recyclerview能不能收到onTouchEvent)
        if (MotionEvent.ACTION_DOWN == action) {
            return false;
        } else {
            //其它事件都不拦截
            return true;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterMatchLeftSlide) {
            AdapterMatchLeftSlide adapterMatchLeftSlide = (AdapterMatchLeftSlide) adapter;
            adapterMatchLeftSlide.clear();
        }
    }

    /**
     * 匹配左滑功能的Adapter
     */
    public static abstract class AdapterMatchLeftSlide<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        private HashMap<Integer, ViewHolder> mMap = new HashMap<>();

        private RecyclerView mRecyclerView;

        /**
         * 记录当前处于开启状态的position
         */
        private int mCurrentOpenPosition = -1;

        /**
         * 子类的onCreateViewHolder方法中，第一行写上super.onCreateViewHolder(viewGroup,i)即可
         */
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            //赋值
            if (null == mRecyclerView && viewGroup instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) viewGroup;
            }
            return onMatchLeftSlideCreateViewHolder(viewGroup, i);
        }

        protected abstract VH onMatchLeftSlideCreateViewHolder(ViewGroup viewGroup, int type);

        /**
         * 子类的onBindViewHolder方法中，第一行写上super.onBindViewHolder(viewHolder,i)即可
         */
        @Override
        public void onBindViewHolder(@NonNull VH viewHolder, @SuppressLint("RecyclerViewImp") final int i) {
            mMap.put(i, viewHolder);
            View itemView = viewHolder.itemView;
            if (itemView instanceof LeftSlideLayout) {
                LeftSlideLayout leftSlideLayout = (LeftSlideLayout) itemView;
                leftSlideLayout.setOnLeftSlideOpenListener(new LeftSlideLayout.OnLeftSlideStateChangedListener() {//NOSONAR
                    @Override
                    public void onOpen() {
                        //当前侧滑菜单正在打开的itemView，再次回到打开位置，不做任何处理
                        if (mCurrentOpenPosition == i) {
                            return;
                        }

                        if (mCurrentOpenPosition != -1) {
                            //也不等于当前位置，那就要关闭打开的item

                            ViewHolder openingViewHolder = mRecyclerView.findViewHolderForAdapterPosition(mCurrentOpenPosition);
                            if (openingViewHolder != null) {
                                onOpenNext(openingViewHolder);
                            } else {
                                ViewHolder vh = mMap.get(mCurrentOpenPosition);
                                onOpenNext(vh);
                            }
                        }
                        mCurrentOpenPosition = i;

                    }

                    private void onOpenNext(ViewHolder openingViewHolder) {
                        if (openingViewHolder != null) {
                            View openingItemView = openingViewHolder.itemView;
                            if (openingItemView instanceof LeftSlideLayout) {
                                LeftSlideLayout openningLeftSlideLayout = (LeftSlideLayout) openingItemView;
                                openningLeftSlideLayout.smoothToOrigin();
                            }
                        }
                    }

                    @Override
                    public void onClose() {
                        if (mCurrentOpenPosition != -1 && mCurrentOpenPosition == i) {
                            mCurrentOpenPosition = -1;
                        }
                    }
                });
                if (i == mCurrentOpenPosition) {
                    leftSlideLayout.scrollToMax();
                } else {
                    leftSlideLayout.scrollToOrigin();
                }
            }
            bindMatchLeftSlideViewHolder(viewHolder, i);
        }

        protected abstract void bindMatchLeftSlideViewHolder(VH viewHolder, int position);

        public void clear() {
            mMap.clear();
        }

        protected void reset() {
            mCurrentOpenPosition = -1;
        }

        public void closeLeftSlideItem() {
            closeLeftSlideItem(false);
        }

        public void closeLeftSlideItem(boolean isSmooth) {
            if (mMap != null) {
                Set<Map.Entry<Integer, ViewHolder>> entries = mMap.entrySet();
                for (Map.Entry<Integer, ViewHolder> entry : entries) {
                    ViewHolder viewHolder = entry.getValue();
                    if (viewHolder != null) {
                        View itemView = viewHolder.itemView;
                        if (itemView instanceof LeftSlideLayout) {
                            LeftSlideLayout leftSlideLayout = (LeftSlideLayout) itemView;
                            if (leftSlideLayout.getScrollX() != 0) {
                                if (isSmooth) {
                                    leftSlideLayout.smoothToOrigin();
                                } else {
                                    leftSlideLayout.scrollToOrigin();
                                }
                                reset();
                            }
                        }
                    }
                }
            }
        }
    }

}