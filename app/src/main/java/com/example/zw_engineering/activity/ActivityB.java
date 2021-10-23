package com.example.zw_engineering.activity;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.zw_engineering.R;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        finish();
        findViewById(R.id.text).postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("mytag");
                ActivityUtils.startActivity(ActivityA.class);
            }
        },1000);

        LogUtils.e("mytag");
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
        LogUtils.e("mytag");
    }

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