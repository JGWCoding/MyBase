package com.example.zw_engineering;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.zw_engineering.util.ShareUtils;

public class Home2Activity extends Activity {
//    <style name="activityTheme" parent="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
//        <item name="android:windowFrame">@null</item><!--边框-->
//        <item name="android:windowIsFloating">true</item><!--是否浮现在activity之上-->
//        <item name="android:windowIsTranslucent">false</item><!--半透明-->
//        <item name="android:windowNoTitle">true</item><!--无标题-->
//        <item name="android:background">@android:color/transparent</item>
//        <item name="android:windowBackground">@android:color/transparent</item><!--背景透明-->
//<!--        <item name="android:backgroundDimEnabled">true</item>&lt;!&ndash;模糊&ndash;&gt;-->
//        <item name="android:backgroundDimEnabled">false</item><!--模糊-->
//    </style>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home2);
        ShareUtils.shareWechatFriend(Home2Activity.this,HomeActivity.imagePath);
    }
}
