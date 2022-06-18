package com.example.zw_engineering;

import android.os.Bundle;
import android.view.View;

import com.dome.base.ui.view.MyCalendarView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.util.ActivityUtils;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class MyDomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dome);

        setContentView(new MyCalendarView(this));

//        setContentView(new DomeViewGroup(this));
        ActivityUtils.startActivity(ItemDragUseActivity.class);
        new TimePickerBuilder(ActivityUtils.getTopActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
//                if (selectDate!=null){
//                    selectDate.select(date);
//                }
//                String date2String = TimeUtils.date2String(date, "yyyy-MM-dd");
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build().show();
    }
}
