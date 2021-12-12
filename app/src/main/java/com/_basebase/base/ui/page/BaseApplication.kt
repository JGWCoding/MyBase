package com._basebase.base.ui.page

import android.app.Activity
import android.app.Application
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils

class BaseApplication : Application() {
    //这里主要提供监听app退到后台的监听
    val appStatusChangedListener = object : Utils.OnAppStatusChangedListener {
        override fun onForeground(activity: Activity?) {
            appIsForeground = true
            //例如: 从后台进入前台,先跳转去广告页,展示广告再finish广告页
        }

        override fun onBackground(activity: Activity?) {
            appIsForeground = false
            //例如:提示用户已切换到后台,金融app提示不安全
        }
    }

    //提供获取当前app是否在前台的状态  或者  AppUtils.isAppForeground()
    var appIsForeground = true


    override fun onCreate() {
        super.onCreate()
        LogUtils.d("app开始初始化资源")


        AppUtils.registerAppStatusChangedListener(appStatusChangedListener)
    }


    override fun onLowMemory() {
        super.onLowMemory()
        LogUtils.d("内存过低,需清理一些缓存内存,例如Glide图片框架清除图片内存缓存")
    }

    override fun onTerminate() {
        super.onTerminate()
        LogUtils.d("app终止执行中")
        AppUtils.unregisterAppStatusChangedListener(appStatusChangedListener)
    }
}