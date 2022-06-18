package com.dome.base.shortcut;

import android.os.Bundle;
import android.widget.TextView;

import com.example.zw_engineering.R;

import androidx.appcompat.app.AppCompatActivity;

public class ShortDome1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_dome1);
        ((TextView)findViewById(R.id.textView)).setText(getClass().getSimpleName());
    }
}