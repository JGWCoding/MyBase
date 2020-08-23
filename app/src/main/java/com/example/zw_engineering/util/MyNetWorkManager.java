package com.example.zw_engineering.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zw_engineering.bean.CalendarBean;
import com.example.zw_engineering.ui.view.LoaddingView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MyNetWorkManager {
    public static void calendarGetReportIDData(final String reportID, final ArrayList<CalendarBean> list, final Runnable success) {
        LoaddingView.show();
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.calendarGetData(reportID);
            }

            @Override
            public void onFail(Throwable t) {
                ToastUtils.showLong("request fail");
                LoaddingView.dismiss();
            }

            @Override
            public void onSuccess(String result) {
                LoaddingView.dismiss();
                LogUtils.e(result);
                if (TextUtils.isEmpty(result) == false && JsonUtils.getInt(result, "code", 0) == 200) {
                    JSONArray data = JsonUtils.getJSONArray(result, "data", null);
                    if (data != null) {
                        list.clear();
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                CalendarBean calendarBean = GsonUtils.fromJson(data.get(i).toString(), CalendarBean.class);
                                list.add(calendarBean);
                            } catch (JSONException e) {
                               LogUtils.e(e.getMessage());
                            }
                        }
                        ToastUtils.showShort("request success");
                        if (success != null) {
                            success.run();
                        }
                    }
                }
            }
        });
    }

    public static void deleteReportType(final int id, final String reportType) {
        //TODO 这里需要知道删除了没有
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.deleteReportType(id, reportType);
            }

            @Override
            public void onSuccess(String result) {
                //{"code":200,"msg":"successful!, reportType"}
                LogUtils.e(result);
            }
        });
    }

    public static void addReportType(final int id, final String reportType) {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.addReportType(id, reportType);
            }

            @Override
            public void onSuccess(String result) {
                //{"code":200,"msg":"successful!, reportType"}
                LogUtils.e(result);
            }
        });
    }
}
