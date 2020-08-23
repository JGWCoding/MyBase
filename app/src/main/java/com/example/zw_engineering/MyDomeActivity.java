package com.example.zw_engineering;

import android.os.Bundle;

import com._basebase.base.touchevent.DomeViewGroup;
import com._basebase.base.ui.view.MyCalendarView;

import androidx.appcompat.app.AppCompatActivity;

public class MyDomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dome);

        setContentView(new MyCalendarView(this));

        setContentView(new DomeViewGroup(this));
    }
}
