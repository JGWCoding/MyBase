package com.example.zw_engineering.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.blankj.utilcode.util.LogUtils;
import com.example.zw_engineering.R;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

//        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                findViewById(R.id.text).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        LogUtils.e("mytag");
//                        ActivityUtils.startActivity(ActivityA.class);
//                    }
//                }, 1000);
////                ActivityUtils.startActivity(ActivityB.class);
//            }
//        });
//        LogUtils.e("mytag");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.e("mytag" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.e("mytag" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.e("mytag");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e("mytag");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e("mytag");
    }
    @Override
    protected void onResume() {
        super.onResume();
//        final FastScrollView scroll = (FastScrollView) findViewById(R.id.scroll);
//        scroll.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scroll.scrollToView(findViewById(R.id.two));
//            }
//        }, 2000);
//        scroll.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scroll.scrollToView(findViewById(R.id.bottom));
//            }
//        }, 5000);
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LogUtils.e("mytag");
//    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("mytag");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("mytag");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("mytag");
    }
}