package com.example.zw_engineering;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.example.zw_engineering.util.MyNetWorkUtils;

import androidx.appcompat.app.AppCompatActivity;
//import com.tencent.bugly.crashreport.CrashReport;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        MyNetWorkUtils.switch_log_interceptor();    //在这初始化，防止多线程进行多次添加
        BarUtils.transparentStatusBar(this);
        Utils.init(this);
        //騰訊檢測
//        Bugly.init(getApplicationContext(), "557bde1921", false);
//        img_bg.setBackground(new BitmapDrawable(BitmapUtils.setBitmapFromDisk(this,R.drawable.bg)));
        //20200210
        //ImageView img_bg = findViewById(R.id.splash_activity_bg);
        //img_bg.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.setBitmapFromDisk(this,R.drawable.bg)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //CrashReport.initCrashReport(getApplicationContext(), "557bde1921", false);
        startActivity(new Intent(this,MainActivity.class));
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this,DomeUse.class));
//                finish();
////                CrashReport.testJavaCrash();
////                throw new NullPointerException("自己的运行异常");
//            }
//        },1000);





    }
}
