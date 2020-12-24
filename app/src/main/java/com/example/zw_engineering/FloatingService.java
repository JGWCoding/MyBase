package com.example.zw_engineering;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.blankj.utilcode.util.ScreenUtils;

import androidx.core.app.NotificationCompat;


public class FloatingService extends Service {

    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;
    Handler mHandler = new Handler();
    private Button mButton;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }
    private void startScreenShot() {
         HomeActivity.mHomeActivity.startScreenShot();
    }


    private GestureDetector.OnGestureListener listener = new GestureDetector.OnGestureListener() {
        // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        /*
         * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
         * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
         *
         * 而onDown也是由一个MotionEventACTION_DOWN触发的，但是他没有任何限制，
         * 也就是说当用户点击的时候，首先MotionEventACTION_DOWN，onDown就会执行，
         * 如果在按下的瞬间没有松开或者是拖动的时候onShowPress就会执行，如果是按下的时间超过瞬间
         * （这块我也不太清楚瞬间的时间差是多少，一般情况下都会执行onShowPress），拖动了，就不执行onShowPress。
         */
        @Override
        public void onShowPress(MotionEvent e) {
            Log.e("DomeUse","onShowPress");
        }
        // 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
        // 轻击一下屏幕，立刻抬起来，才会有这个触发
        // 从名子也可以看出,一次单独的轻击抬起操作,当然,如果除了Down以外还有其它操作,那就不再算是Single操作了,所以这个事件 就不再响应
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("DomeUse","onSingleTapUp");
            startScreenShot();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    ShareUtils.shareWechatFriend(FloatingService.this,HomeActivity.imagePath);
                    Intent intent = new Intent(FloatingService.this, HomeActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(),
                            1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new NotificationCompat.Builder(FloatingService.this, "10")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("条用")
                            .setContentText("截图")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_ALARM)
                            //设置全屏通知后，发送通知直接启动Activity
                            .setFullScreenIntent(pendingIntent, true)
                            .build();
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.notify(10, notification);

                }
            },1000);
            return false;
        }
        // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("DomeUse","onScroll");
            return false;
        }
        // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
        @Override
        public void onLongPress(MotionEvent e) {
            if (mLayoutParams.width!=ScreenUtils.getScreenWidth()){
                mLayoutParams.width = ScreenUtils.getScreenWidth();
                mLayoutParams.height = ScreenUtils.getScreenHeight();
            }else {
                mLayoutParams.width = 150;
                mLayoutParams.height = 150;
            }
                mWindowManager.updateViewLayout(mButton, mLayoutParams);
            Log.e("DomeUse","onLongPress");
        }
        // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("DomeUse","onFling");
            return false;
        }

    };
    GestureDetector gestureDetector;
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            // 获取WindowManager服务
//            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager = HomeActivity.mHomeActivity.getWindowManager();
            // 新建悬浮窗控件
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View displayView = layoutInflater.inflate(R.layout.activity_home, null);
            displayView.setOnTouchListener(new FloatingOnTouchListener());
            displayView.setEnabled(false);
//            Button button = displayView.findViewById(R.id.button);
            mButton = new Button(getApplicationContext());
            mButton.setWidth(150);
            mButton.setHeight(150);
            mButton.setText("截图");
            mButton.setBackgroundColor(Color.RED);
            mButton.setOnTouchListener(new FloatingOnTouchListener());
            gestureDetector = new GestureDetector(this,listener);
            // 设置LayoutParam
            mLayoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.width = 150;
            mLayoutParams.height = 150;
//            mLayoutParams.x = 100;
//            mLayoutParams.y = 100;
            mLayoutParams.flags = mLayoutParams.FLAG_NOT_TOUCH_MODAL
                    | mLayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_FULLSCREEN;
            mLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
            // 将悬浮窗控件添加到WindowManager
            mWindowManager.addView(mButton, mLayoutParams);
//            mWindowManager.addView(displayView, mLayoutParams);
        }
    }
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    mLayoutParams.x = mLayoutParams.x + movedX;
                    mLayoutParams.y = mLayoutParams.y + movedY;

                    // 更新悬浮窗控件布局
                    mWindowManager.updateViewLayout(view, mLayoutParams);
                    break;
                default:
                    break;
            }

            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
