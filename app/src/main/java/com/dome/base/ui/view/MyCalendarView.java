package com.dome.base.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zw_engineering.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyCalendarView extends LinearLayout {
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView  textDate;
    private  GridView gridView;
    private Calendar mCalendar = Calendar.getInstance();
    public MyCalendarView(Context context) {
        this(context,null);
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context){
        bindControl(context);
    }

    private void bindControl(Context context) {
        LayoutInflater.from(context).inflate(R.layout.my_calendar_view,this,true);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        textDate = findViewById(R.id.calendar_title);
        gridView = findViewById(R.id.calendar_grid);

        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.MONTH,-1);
                renderCalendar();
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.MONTH,1);
                renderCalendar();
            }
        });
        renderCalendar();
    }

    private void renderCalendar() { //刷新日历
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        textDate.setText(sdf.format(mCalendar.getTime()));


        ArrayList<Date> cells = new ArrayList<>();
        int currentMonth = mCalendar.get(Calendar.MONTH);
        Calendar calendar = (Calendar) mCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH,1);//把当前日期置于为当月1号
        int preDays = calendar.get(Calendar.DAY_OF_WEEK) - 1; //得到1号距离周天有多少天
        calendar.add(Calendar.DAY_OF_MONTH,-preDays);
        int maxCellCount = 6*7;
        while (cells.size()<maxCellCount){

            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
            if (calendar.get(Calendar.MONTH)!=currentMonth&&cells.size()%7==0){
                break;
            }
        }
        gridView.setAdapter(new CalendarAdapter(getContext(),cells));

    }

    private class CalendarAdapter extends ArrayAdapter<Date>{
        LayoutInflater mLayoutInflater;
        public CalendarAdapter(@NonNull Context context,ArrayList<Date> days) {
            super(context, R.layout.calendar_text_day,days);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView==null){
                convertView = mLayoutInflater.inflate(R.layout.calendar_text_day,parent,false);
            }
            int day = date.getDate();
            ((TextView)convertView) .setText(String.valueOf(day));

            if (date.getMonth() == mCalendar.get(Calendar.MONTH)){
                ((TextView)convertView).setTextColor(Color.BLACK);
            }else {
                ((TextView)convertView).setTextColor(Color.GRAY);
            }
            //当天进行特别 展示
            Date now = new Date();
            if (now.getMonth()==mCalendar.get(Calendar.MONTH)&&now.getDate()==date.getDate()&&now.getMonth()==date.getMonth()&&now.getYear()==date.getYear()){
                    ((TextView)convertView).setTextColor(Color.RED);
            }
            return convertView;
        }
    }
}
