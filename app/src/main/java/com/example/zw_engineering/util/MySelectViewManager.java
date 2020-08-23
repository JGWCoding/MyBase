package com.example.zw_engineering.util;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.ActivityUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MySelectViewManager {
    public interface SelectOption{
        void select(int option);
    }
    public interface SelectDate{
        void select(Date date);
    }
    public void  dateSelect(final SelectDate selectDate){
         new TimePickerBuilder(ActivityUtils.getTopActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (selectDate!=null){
                    selectDate.select(date);
                }
//                String date2String = TimeUtils.date2String(date, "yyyy-MM-dd");
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build().show();
    }
    public MySelectViewManager init(Context context, List list, final SelectOption selectOption){
        OptionsPickerView optionsPickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (selectOption!=null){
                    selectOption.select(options1);
                }
            }
        }).build();
        optionsPickerView.setPicker(list);
        optionsPickerView.show();
        return this;
    }
    public MySelectViewManager init(Context context, final SelectOption selectOption,String...args){
        OptionsPickerView optionsPickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (selectOption!=null){
                    selectOption.select(options1);
                }
            }
        }).build();
        optionsPickerView.setPicker(Arrays.asList(args));
        optionsPickerView.show();
        return this;
    }
}
