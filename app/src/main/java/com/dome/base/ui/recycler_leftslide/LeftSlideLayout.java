package com.dome.base.ui.recycler_leftslide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.blankj.utilcode.util.LogUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 使用
 * <?xml version="1.0" encoding="utf-8"?>
 * <包名.LeftSlideLayout xmlns:android="http://schemas.android.com/apk/res/android"
 * xmlns:tools="http://schemas.android.com/tools"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:layout_marginStart="10dp"
 * android:layout_marginTop="10dp"
 * android:layout_marginEnd="10dp"
 * android:background="@drawable/shape_package_bg">
 * <p>
 * <RelativeLayout
 * android:id="@+id/root_view"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:padding="@dimen/dp_15"
 * android:background="@drawable/common_item_bg"
 * android:tag="content">
 * //TODO 这里写条目内容  tag="content"
 * </RelativeLayout>
 * <p>
 * <LinearLayout
 * android:layout_width="wrap_content"
 * android:layout_height="match_parent"
 * android:layout_marginBottom="1dp"
 * android:layout_marginTop="1dp"
 * android:orientation="horizontal"
 * android:tag="right">
 * //TODO 这里写左滑出来的内容 tag="right"
 * </LinearLayout>
 * <p>
 * </包名.LeftSlideLayout>
 */
public class LeftSlideLayout extends FrameLayout {

    /**
     * 滚动到多少时，显示全部的附加view
     */
    private static final float RATIO = 0.5f;
    /**
     * 弹性滑动的时间
     */
    private static final int DURATION = 300;

    /**
     * 按下时的x坐标
     */
    private int mDownX;

    private int mDownY;

    private int mScaledTouchSlop;   //默认移动范围值 -- 超过这个值认为移动过
    /**
     * 屏幕外view
     */
    private View mRightView;
    private View mContentView;

    /**
     * 上一下触摸点的y坐标
     */
    private int mLastTouchX;
    private Scroller mScroller;
    private int mWhichDirectionSlide;//表示返回-1，表示上滑；-2表示下滑 0非左滑也非右滑(滑动距离太小,不好判断滑动方向)1表示左滑；2表示右滑；3表示滑动停止

    private OnLeftSlideStateChangedListener mOnLeftSlideStateChangedListener;

    private boolean isHaveExecutedHoronzitalMove = false;

    public LeftSlideLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(context);
        //设置可点击，这样它的super.onTouchEvent一定是返回true的
        setClickable(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //mRightView有可能为空
        mRightView = this.findViewWithTag("right");
        mContentView = this.findViewWithTag("content");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mRightView != null && mContentView != null) {
            super.onLayout(changed, l, t, r, b);
            //对mRightView重新Layout
            mRightView.layout(mRightView.getLeft() + getMeasuredWidth(), mRightView.getTop(),
                    mRightView.getRight() + getMeasuredWidth(), mRightView.getBottom());
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //竖直滑动是外部RecyclerView要处理的事件
        mWhichDirectionSlide = isWhichDirectionSlide(ev);
        if (mWhichDirectionSlide < 0) {
            //上下滑
            smoothRightLocation();
            //父布局拦截
            requestDisallowIntercept(false);
        } else {
            //down事件或左右滑的情况，父布局不拦截
            requestDisallowIntercept(true);
            //为什么水平滑动判断不能写在onInterceptTouchEvent中？
            //因为如果其子view不消耗事件，那么onInterceptTouchEvent方法将不会再执行
        }
        return super.dispatchTouchEvent(ev);
    }

    //滑动到合适的位置
    private void smoothRightLocation() {
        if (mRightView != null) {
            if (getScrollX() != 0 && getScrollX() != mRightView.getWidth()) {
                //滑动超出rightView范围,修复显示
                if (getScrollX() > mRightView.getWidth()) {
                    smoothToMax();
                } else {
                    smoothToOrigin();
                }
            }
        }
    }

    public void scrollToMax() {
        if (mRightView != null) {
            scrollTo(mRightView.getWidth(), 0);
            invalidate();
        }
    }

    public void scrollToOrigin() {
        scrollTo(0, 0);
        invalidate();
    }

    public void smoothToMax() { //显示完全rightView
        mScroller.startScroll(getScrollX(), 0, mRightView.getWidth() - getScrollX(), 0, DURATION);
        invalidate();
//        if (mOnLeftSlideStateChangedListener != null) {
//            mOnLeftSlideStateChangedListener.onOpen();
//        }
    }

    public void smoothToOrigin() {  //隐藏rightView
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, DURATION);
        invalidate();
//        if (mOnLeftSlideStateChangedListener != null) {
//            mOnLeftSlideStateChangedListener.onClose();
//        }
    }

    /**
     * 内部拦截法避免事件传递流程冲突
     */
    private void requestDisallowIntercept(boolean isDisallow) {
        ViewGroup parent = (ViewGroup) getParent();
        //不需要判空，因为instantof关键字会覆盖为空的情况
        if (parent instanceof RecyclerView) {
            parent.requestDisallowInterceptTouchEvent(isDisallow);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //在左右滑动，我就拦截
        if (mWhichDirectionSlide > 0 || isHaveExecutedHoronzitalMove) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 返回1，表示左滑；返回0，表示即非左滑也非右滑；返回2，表示右滑；返回3，表示
     * 返回-1，表示上滑；返回-2，表示下滑
     */
    private int isWhichDirectionSlide(MotionEvent ev) {//NOSONAR
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: //NOSONAR
                //正在执行滑动时，发现仍旧在滑动，直接中止动画
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mDownX = x;
                mDownY = y;
                isHaveExecutedHoronzitalMove = false;
                break;
            case MotionEvent.ACTION_MOVE://NOSONAR
                int dx = x - mDownX;
                int dy = y - mDownY;

                //偏移量只要有一个达到滑动最小距离就可以
                if ((Math.abs(dx) >= mScaledTouchSlop || Math.abs(dy) >= mScaledTouchSlop)) {
                    //水平距离小于垂直距离时，视为上下滑
                    if (Math.abs(dx) < Math.abs(dy)) {
                        if (dy < -mScaledTouchSlop) {
                            //上滑
                            return -1;
                        } else if (dy > mScaledTouchSlop) {
                            //下滑
                            return -2;
                        }
                        isHaveExecutedHoronzitalMove = false;
                    } else if (Math.abs(dx) > Math.abs(dy)) {
                        //水平滑动
                        if (dx < -mScaledTouchSlop) {
                            //左滑
                            return 1;
                        } else if (dx > mScaledTouchSlop) {
                            //右滑
                            return 2;
                        }
                        isHaveExecutedHoronzitalMove = true;
                    }
                } else {
                    isHaveExecutedHoronzitalMove = false;
                }
                break;
            case MotionEvent.ACTION_UP://NOSONAR
                //且左右滑过
                if (mRightView != null && isHaveExecutedHoronzitalMove && (0 == getScrollX() || mRightView.getWidth() == getScrollX())) {
                    //手指抬起时，水平滚动范围已经到了初始位置或最大位置，那此时仍旧视为是左右滑
                    return 3;
                }
                break;
            default:
        }

        return 0;
    }

    /**
     * 此onTouchEvent方法的执行，有可能是水平滑动事件拦截后执行的，也可能是子View并不处理事件回传到它执行的
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {//NOSONAR
        LogUtils.e(event.getAction());
        //当前事件是识别到左滑之后的处理
        if (mRightView != null) {
            int x = (int) event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastTouchX = x;
                    break;
                case MotionEvent.ACTION_MOVE://NOSONAR
                    if (mLastTouchX != 0 && mRightView != null) {
                        int dx = x - mLastTouchX;
                        if (Math.abs(dx) <= mScaledTouchSlop) {
                            return super.onTouchEvent(event);
                        }
                        //仍旧在左滑
                        //滑到附加View宽度的时候，将不能再继续往左滑
                        if (dx < 0) {
                            if (getScrollX() - dx < mRightView.getWidth()) {//NOSONAR
                                //本控件跟着左滑
                                scrollBy(-dx, 0);
                            } else {
                                //使其恰好滚动到附加view的宽度
                                scrollTo(mRightView.getWidth(), 0);
                            }
                        } else if (dx > 0) {
                            //正在右滑
                            if (getScrollX() - dx > 0) {//NOSONAR
                                scrollBy(-dx, 0);
                            } else {
                                scrollTo(0, 0);
                            }
                        }
                    }
                    mLastTouchX = x;
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //完全显示rightView还是隐藏
                    LogUtils.e("wo up");
                    return actionUpCase();

                default:
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 拆分方法
     */
    private boolean actionUpCase() {
        //手指抬起来之后，判断一下当前滚动的位置
        int scrollX = getScrollX();
        if (scrollX > mRightView.getWidth() * RATIO) {
            //继续左滑
            mScroller.startScroll(scrollX, 0, mRightView.getWidth() - scrollX, 0, DURATION);
            invalidate();

            if (mOnLeftSlideStateChangedListener != null) {
                mOnLeftSlideStateChangedListener.onOpen();
            }

        } else {
            //回滚到初始位置
            mScroller.startScroll(scrollX, 0, -scrollX, 0, DURATION);
            invalidate();

            if (mOnLeftSlideStateChangedListener != null) {
                mOnLeftSlideStateChangedListener.onClose();
            }
        }

        mLastTouchX = 0;
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 设置左滑菜单打开的监听，这个只供内部使用
     */
    public void setOnLeftSlideOpenListener(OnLeftSlideStateChangedListener listener) {
        this.mOnLeftSlideStateChangedListener = listener;
    }

    public interface OnLeftSlideStateChangedListener {
        /**
         * 侧滑菜单打开的回调
         */
        void onOpen();

        /**
         * 侧滑菜单关闭的回调
         */
        void onClose();
    }
}
