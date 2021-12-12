package com._basebase.base.ui.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {

    private static boolean isMiUi = false;

    NetworkUtils.OnNetworkStatusChangedListener mOnNetworkStatusChangedListener = new NetworkUtils.OnNetworkStatusChangedListener() {
        @Override
        public void onDisconnected() {
            onNetworkChanged(false);
        }

        @Override
        public void onConnected(NetworkUtils.NetworkType networkType) {
            onNetworkChanged(true);
        }
    };

    /**
     * 网络联网状态改变监听,需注意监听的生命周期,只要页面执行onstop就不监听了
     *
     * @param isNetConnect
     */
    protected void onNetworkChanged(boolean isNetConnect) {
        LogUtils.d("网络连接状态改变:当前联网状态" + isNetConnect);
    }

    /**
     * 在setContentView之前,实现对window的操作,因少数数类需要,不强制实现
     */
    protected void initWindow() {

    }

    /**
     * 是否需要解析intent传过来的数据
     *
     * @param intent
     */
    protected void parseIntent(Intent intent) {

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void addListener();

    protected abstract void initOberver();

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(getLayoutId());
        LogUtils.d("current_activity:" + getClass().getSimpleName() + "       具体所在类=" + getClass().getName());
        if (getIntent() != null) {
            parseIntent(getIntent());
        }
        initView();     //初始化view信息
        addListener();  //添加监听
        initOberver();  //添加ViewModel 数据监听
        initData();     //初始化数据
    }


    /**
     * 打印启动activity的信息,方便定位所在的页面
     */
    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("current_activity:" + getClass().getSimpleName() + "       具体所在类=" + getClass().getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkUtils.registerNetworkStatusChangedListener(mOnNetworkStatusChangedListener);
        LogUtils.d("current_activity:" + getClass().getSimpleName() + "       具体所在类=" + getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("current_activity:" + getClass().getSimpleName() + "       具体所在类=" + getClass().getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkUtils.unregisterNetworkStatusChangedListener(mOnNetworkStatusChangedListener);//页面看不见就不在监听网络变化
        LogUtils.d("current_activity:" + getClass().getSimpleName() + "       具体所在类=" + getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("current_activity:" + getClass().getSimpleName() + "       具体所在类=" + getClass().getName());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onScreenChanged(newConfig);
    }

    /**
     * 主要用来监听横竖屏变化,不知道能不能监听现在的分屏屏幕变小
     *
     * @param newConfig
     */
    protected void onScreenChanged(Configuration newConfig) {
        LogUtils.v("是否竖屏:" + ScreenUtils.isPortrait());
    }

    /**
     * 设置小米黑色状态栏字体
     */
    @SuppressLint("PrivateApi")
    private void setMIUIStatusBarDarkMode() {
        if (isMiUi) {
            Class<? extends Window> clazz = getWindow().getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(getWindow(), darkModeFlag, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 静态域，获取系统版本是否基于MIUI
     */
    static {
        try {
            @SuppressLint("PrivateApi") Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            isMiUi = version.compareTo("V6") >= 0 && Build.VERSION.SDK_INT < 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置魅族手机状态栏图标颜色风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    public static boolean setMeiZuDarkMode(Window window, boolean dark) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 24) {
            return false;
        }
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @SuppressLint("InlinedApi")
    private int getStatusBarLightMode() {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMiUi) {
                result = 1;
            } else if (setMeiZuDarkMode(getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    @SuppressLint("InlinedApi")
    protected void setStatusBarDarkMode() {
        int type = getStatusBarLightMode();
        if (type == 1) {
            setMIUIStatusBarDarkMode();
        } else if (type == 2) {
            setMeiZuDarkMode(getWindow(), true);
        } else if (type == 3) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}