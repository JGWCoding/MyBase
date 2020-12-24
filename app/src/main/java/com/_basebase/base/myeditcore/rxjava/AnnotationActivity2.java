//package com._basebase.base.myeditcore.rxjava;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//
//import com.example.zw_engineering.R;
//import com.snail.xx_annotion.homework.Autowired;
//import com.snail.xx_annotion.homework.IntentExtraInjectUtil;
//import com.snail.xx_annotion.inject.InjectView;
//import com.snail.xx_annotion.inject.InjectViewUtil;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class AnnotationActivity2 extends AppCompatActivity {
//
//    @InjectView(R.id.tv_b)
//    private TextView mName;
//
//    @Autowired("name")
//    private String name;
//    @Autowired("isBoy")
//    private boolean isBoy;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_annotation2);
//        try {
//            InjectViewUtil.inject(this);
//            IntentExtraInjectUtil.injectValue(this);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
////        String name = getIntent().getStringExtra("name");
////        boolean boy = getIntent().getBooleanExtra("boy", false);
//        Log.d("测试注入结果", "name : " + name + "\tboy = " + isBoy);
//        mName.setText(name + "\t" + isBoy);
//    }
//}