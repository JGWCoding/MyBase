package com.dome.base.myeditcore.mybutterknife;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zw_engineering.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 手写ButterKnife
 * 涉及到的知识点 注解 反射
 *
 */
@MyContentView(R.layout.activity_main)
public class DomeUseActivity extends AppCompatActivity {

    @MyBindView(R.id.main_activity_login)
    TextView textView;

    @OnClick({R.id.main_activity_login})  //可注入多个view
    public void onClick(View view) {    //这个方法在框架中写死了
        switch (view.getId()) {
            case R.id.main_activity_login:
                textView.setText("我是手写ButterKnife框架");
                break;
            default:
                Log.e("DomeUseActivity", "view not found");
                break;
        }
    }

    @OnLongClick(R.id.main_activity_login)
    public void onLongClick(View view) { //这个方法在框架中写死了
        Toast.makeText(this, "长按", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // TODO 注入 当前 Activity
        // 这里是重点 拿到当前 this 之后 利用反射
        ButterKnife.inject(this);
    }
}