package com.dome.base.ui.recycler_pull_upanddown;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 创建者：jizhan zhang
 * <p>
 * 描述：可上下拉的RelativeLayout，此view不具有通用性,子view必须是RecyclerView
 * <p>
 * 日期：2019-06-03 11:51
 */
public class RelativeWithPullLayout extends RelativeLayout {

    /**
     * 可下拉的最大距离，后期如果想要更好的通用性，此值可通过自定义属性来设置
     */
    private static final int PULL_DISTANCE = 200;
    /**
     * 平滑滚动的事件
     */
    private static final int SMOOTH_DURATION = 800;

    private int mScaleTouchSlop;
    private int mLastY;
    private int mLastTouchY;

    private boolean isPullDown = false;
    private boolean isPullUp = false;

    private Scroller mScroller;
    private int mDownY;
    private DownRefreshIconView mDownRefreshIconView;
    private UpRefreshIconView mUpRefreshIconView;

    private OnPullListener mOnPullListener;

    private int mDownDistance;
    private int mUpDistance;

    /**
     * 既非上拉，也非下拉
     */
    private static final int TYPE_NONE = 0;
    /**
     * 下拉
     */
    private static final int TYPE_DOWN = 1;
    /**
     * 上拉
     */
    private static final int TYPE_UP = 2;


    /**
     * 当前的上下拉类型，
     * fixed：初始值置为下拉，如再次上下拉肯定要通过触发mPullType被赋值为type_down
     */
    private int mPullType = TYPE_DOWN;

    /**
     * 是否正在刷新
     */
    private boolean isListRefreshing = false;
    /**
     * 支持的上下拉类型，down表示只支持下拉；up表示只支持上拉；all，表示上下拉都支持
     */
    private int mPullLayoutPullType = 3;//1下拉2上拉 3都可以上下拉

    public RelativeWithPullLayout(Context context) {
        this(context, null);
    }

    public RelativeWithPullLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeWithPullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //初始化上下拉的控件
        DownRefreshLayout downRefreshIconView = new DownRefreshLayout(getContext());
        UpRefreshLayout upRefreshLayout = new UpRefreshLayout(getContext());
        addDownRefreshIconView(downRefreshIconView);
        addUpRefreshIconView(upRefreshLayout);
    }

    /**
     * 设置更新icon，传入null将会导致没有上拉布局或者下拉布局
     * 可以设置自己的刷新样式
     */
    public void setRefreshIconView(DownRefreshIconView downRefreshIconView, UpRefreshIconView upRefreshIconView) {
        addDownRefreshIconView(downRefreshIconView);
        addUpRefreshIconView(upRefreshIconView);
        requestLayout();
    }


    /**
     * 添加下拉布局
     */
    private void addDownRefreshIconView(DownRefreshIconView downRefreshIconView) {
        View downView = findViewWithTag("downView");
        if (downRefreshIconView != null) {
            //已经存在一个downView了，那就移除掉存在的
            if (downView != null) {
                //当前存在的下拉布局为默认布局，要设置的新布局仍旧为默认布局，那就无需再删除并重新添加
                if (downView instanceof DownRefreshLayout && downRefreshIconView instanceof DownRefreshLayout) {
                    return;
                }
                removeView(downView);
            }
            this.mDownRefreshIconView = downRefreshIconView;
            //当前不存在下拉布局，或者有新的下拉布局时，将此布局添加到父布局中
            LayoutParams downLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            downLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            downRefreshIconView.setLayoutParams(downLayoutParams);
            downRefreshIconView.setTag("downView");
            //添加到0位置
            this.addView(downRefreshIconView, 0);
        } else {
            if (downView != null) {
                removeView(downView);
            }
            this.mDownRefreshIconView = null;
        }
    }

    private void addUpRefreshIconView(UpRefreshIconView upRefreshIconView) {
        View upView = findViewWithTag("upView");
        if (upRefreshIconView != null) {
            if (upView != null) {
                //与下拉布局同理
                if (upView instanceof UpRefreshLayout && upRefreshIconView instanceof UpRefreshLayout) {
                    return;
                }
                removeView(upView);
            }
            this.mUpRefreshIconView = upRefreshIconView;
            LayoutParams upLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            upLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            upRefreshIconView.setLayoutParams(upLayoutParams);
            upRefreshIconView.setTag("upView");
            this.addView(upRefreshIconView, 0);
        } else {
            if (upView != null) {
                removeView(upView);
            }
            this.mUpRefreshIconView = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //先按默认布局放置，之后再对第一个元素重新layout
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        //为什么childCount要大于1，因为要确保该布局里面要有RecyclerView这个子元素
        if (childCount > 1) {
            //downView
            View downView = findViewWithTag("downView");
            if (downView != null) {
                int measuredHeight = downView.getMeasuredHeight();
                initDownDistance(measuredHeight);
                downView.layout(downView.getLeft(), -measuredHeight,
                        downView.getRight(), 0);
            }
        }
        if (childCount > 1) {
            View upView = findViewWithTag("upView");
            if (upView != null) {
                //upView
                int measuredHeight = upView.getMeasuredHeight();
                initUpDistance(measuredHeight);
                //这里的减t，是我凭感觉试出来的，但我并没有完全理解，为什么要减t
                upView.layout(upView.getLeft(), b - t,
                        upView.getRight(), b - t + measuredHeight);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mScaleTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    private void initDownDistance(int height) {
        mDownDistance = height > PULL_DISTANCE ? height + 20 : PULL_DISTANCE;
    }

    private void initUpDistance(int height) {
        mUpDistance = height > PULL_DISTANCE ? height + 20 : PULL_DISTANCE;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDownRefreshIconView != null && isRecyclerViewInitState() && isPullDown) {
            //列表处于初始状态，且正在往下拉，拦截事件
            mPullType = TYPE_DOWN;
            return true;
        }
        if (mUpRefreshIconView != null && isRecyclerViewEndState() && isPullUp) {
            //处于列表最顶部且正在往上拉，拦截事件
            mPullType = TYPE_UP;
            return true;
        }
        //既不是上拉，也不是下拉
        mPullType = TYPE_NONE;

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //正在刷新不再允许滚动列表
        //fixed：正在刷新，或者正在弹性滑动时不接受多余的时间
        if (isListRefreshing || !mScroller.isFinished()) {
            mDownY = 0;
            return true;
        }
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(y);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(y);
                break;
            case MotionEvent.ACTION_UP:

                isPullDown = false;
                isPullUp = false;
                break;
            default:
        }

        //记录上一次的y坐标
        mLastY = y;

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 拆分方法
     */
    private void actionDown(int y) {
        isPullDown = false;
        isPullUp = false;
        mPullType = TYPE_NONE;
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        mDownY = y;
    }

    /**
     * 拆分方法
     */
    private void actionMove(int y) {

        //没有捕获到down坐标的话,就拿当次的move事件y坐标来设置该值
        if (0 == mDownY) {

            isPullDown = false;
            isPullUp = false;
            mPullType = TYPE_NONE;

            mDownY = y;
            return;
        }

        int dy = y - mDownY;

        //这是往下拉了
        if (dy > mScaleTouchSlop) {
            //有下拉布局时，才去标识这是下拉
            if (mDownRefreshIconView != null) {
                isPullDown = true;
            } else {
                isPullDown = false;
            }
        } else if (dy < -mScaleTouchSlop) {
            //有上拉布局时，才去标识这是上拉
            if (mUpRefreshIconView != null) {
                //往上拉
                isPullUp = true;
            } else {
                isPullUp = false;
            }
        } else {
            isPullDown = false;
            isPullUp = false;
        }
    }



    /**
     * 是否可滚动
     */
    public boolean isRecyclerScrollable() {
        int childCount = getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {

                    RecyclerView recyclerView = (RecyclerView) child;
                    //在列表不可滚动时，range小于height
                    return recyclerView.computeVerticalScrollRange() > recyclerView.getHeight();
                }
            }
        }
        return false;
    }

    private boolean isRecyclerViewEndState() {

        int childCount = getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {

                    RecyclerView recyclerView = (RecyclerView) child;
                    //当前的垂直滚动距离
                    int offset = recyclerView.computeVerticalScrollOffset();
                    //RecyclerView所能达到的最大滚动距离
                    int range = recyclerView.computeVerticalScrollRange();
                    //此方法调用时是在处理触摸事件时调用，所以肯定会有高度值
                    int height = recyclerView.getHeight();
                    int paddingTop = recyclerView.getPaddingTop();
                    int paddingBottom = recyclerView.getPaddingBottom();

                    //偏移量+控件高度在1像素以内，就认为是到达了底部
                    if (Math.abs((range + paddingTop + paddingBottom) - (offset + height)) <= 1) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//NOSONAR

        //有一种情况，当下拉时，子view并不处理这个事件，还是回到了当前ViewGroup处理得事件，
        //之后move事件都不会再走拦截方法了，直接有dispatchTouchEvent到了onTouchEvent方法，
        //此时的PullType并没有被设置新值
        if (TYPE_NONE == mPullType) {
            if (mDownRefreshIconView != null && isRecyclerViewInitState() && isPullDown) {
                //列表处于初始状态，且正在往下拉，拦截事件
                mPullType = TYPE_DOWN;
            }

            if (mUpRefreshIconView != null && isPullUp) {
                //处于列表最顶部且正在往上拉，拦截事件
                if (!isRecyclerScrollable() || isRecyclerViewEndState()) {//NOSONAR
                    mPullType = TYPE_UP;
                }
            }
        }

        if (mPullType != TYPE_NONE) {

            //当前触摸事件的y坐标
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //因为是拦截来的，所以有可能没有down事件；
                    break;
                case MotionEvent.ACTION_MOVE://NOSONAR
                    if (0 == mLastTouchY) {
                        mLastTouchY = mLastY;
                    }
                    //偏移的距离
                    int deltaY = y - mLastTouchY;

                    if (TYPE_DOWN == mPullType) {
                        //下拉时，上拉到起始位置就不要再动
                        if (deltaY < 0 && 0 == getScrollY()) {
                            //下拉时的上滑
                            break;
                        }

                        int moveScrollY = getScrollY();
                        if (moveScrollY != 0) {
                            //mPullType为TYPE_DOWN，mDownRefreshIconView一定不为空
                            if (moveScrollY > -mDownDistance) {
                                mDownRefreshIconView.setType(DownRefreshIconView.TYPE_DOWN);
                            } else {
                                mDownRefreshIconView.setType(DownRefreshIconView.TYPE_CAN_REFRESH);
                            }
                        }
                    } else if (TYPE_UP == mPullType) {
                        //上拉时的下滑
                        if (deltaY > 0 && 0 == getScrollY()) {
                            break;
                        }

                        int moveScrollY = getScrollY();
                        if (moveScrollY != 0) {
                            //mUpRefreshIconView为TYPE_UP时，mUpRefreshIconView也一定不为空
                            if (moveScrollY < mUpDistance) {
                                mUpRefreshIconView.setType(UpRefreshIconView.TYPE_UP);
                            } else {
                                mUpRefreshIconView.setType(UpRefreshIconView.TYPE_CAN_REFRESH);
                            }
                        }
                    }

                    scrollBy(0, -deltaY);
                    mLastTouchY = y;
                    break;
                case MotionEvent.ACTION_UP://NOSONAR
                    //弹性回去，或者弹性至最大到加载状态
                    int scrollY = getScrollY();
                    if (scrollY != 0) {

                        if (TYPE_DOWN == mPullType) {

                            if (scrollY > -mDownDistance) {
                                isListRefreshing = false;
                                mDownRefreshIconView.setType(DownRefreshIconView.TYPE_DOWN);
                                smoothScrollBy(0, -scrollY);
                            } else {
                                isListRefreshing = true;
                                mDownRefreshIconView.setType(DownRefreshIconView.TYPE_REFRESHING);
                                smoothScrollBy(0, -mDownDistance - scrollY);

                                if (mOnPullListener != null) {//NOSONAR
                                    mOnPullListener.onRefreshing(true);
                                }
                            }
                        } else if (TYPE_UP == mPullType) {
                            if (scrollY < mUpDistance) {
                                isListRefreshing = false;
                                mUpRefreshIconView.setType(UpRefreshIconView.TYPE_UP);
                                smoothScrollBy(0, -scrollY);
                            } else {
                                isListRefreshing = true;
                                mUpRefreshIconView.setType(DownRefreshIconView.TYPE_REFRESHING);
                                smoothScrollBy(0, mUpDistance - scrollY);
                                if (mOnPullListener != null) {//NOSONAR
                                    mOnPullListener.onRefreshing(false);
                                }
                            }
                        }
                    }

                    mLastTouchY = 0;
                    break;
                default:
            }
        }

        return true;
    }

    /**
     * 进行平滑滚动
     */
    private void smoothScrollBy(int dx, int dy) {
        if (mScroller != null) {
            mScroller.startScroll(0, getScrollY(), dx, dy, SMOOTH_DURATION);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


    private boolean isRecyclerViewInitState() {
        int childCount = getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child instanceof RecyclerView) {

                    RecyclerView recyclerView = (RecyclerView) child;
                    //当前的垂直滚动距离
                    int offset = recyclerView.computeVerticalScrollOffset();
                    if (0 == offset) {
                        //垂直滚动距离为0，说明当前的RecyclerView就在滚动为0的初始状态
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public void setRefreshing(boolean isRefreshing) {
        if (mPullType == TYPE_DOWN) {
            if (mDownRefreshIconView != null) {
                if (isRefreshing) {
                    isListRefreshing = true;
                    smoothScrollBy(0, -mDownDistance - getScrollY());
                    mDownRefreshIconView.setType(DownRefreshIconView.TYPE_REFRESHING);
                } else {
                    isListRefreshing = false;
                    smoothScrollBy(0, -getScrollY());
                    mDownRefreshIconView.setType(DownRefreshIconView.TYPE_DOWN);
                }
            }

        } else if (mPullType == TYPE_UP && mUpRefreshIconView != null) {
            if (isRefreshing) {
                isListRefreshing = true;
                smoothScrollBy(0, mUpDistance - getScrollY());
                mUpRefreshIconView.setType(UpRefreshIconView.TYPE_REFRESHING);
            } else {
                isListRefreshing = false;
                smoothScrollBy(0, -getScrollY());
                mUpRefreshIconView.setType(UpRefreshIconView.TYPE_UP);
            }
        }
    }

    /**
     * 设置上下拉刷新监听
     */
    public void setOnPullListener(OnPullListener listener) {
        this.mOnPullListener = listener;
    }

    public interface OnPullListener {
        /**
         * 正在刷新
         *
         * @param isPullDown 是否是下拉 true 为下拉  false为上拉
         */
        void onRefreshing(boolean isPullDown);
    }
}
