package com.example.zw_engineering.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.example.zw_engineering.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyNetWorkUtils {
    static HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
    static HashMap<String, String> cacheData = new HashMap<>();
    static boolean is_add_logInterceptor = false;
    //    static OkHttpClient client = new OkHttpClient.Builder();
    static OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();

    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    //    public static final MediaType JSON = MediaType.get("text/html; charset=utf-8");
    private static String url_base = "http://203.160.94.175/ZH_Engineering/public/app/";
    private static String url_login = "http://203.160.94.175/ZH_Engineering/public/app/login";
    private static String url_get_search = "http://203.160.94.175/ZH_Engineering/public/app/get_reportlist";
    private static String url_del_search = "http://203.160.94.175/ZH_Engineering/public/app/delete_report";
    private static String url_get_info = "http://203.160.94.175/ZH_Engineering/public/app/get_reportinfo";
    private static String url_get_details = "http://203.160.94.175/ZH_Engineering/public/app/get_reportdetails";
    private static String url_get_select_item = "http://203.160.94.175/ZH_Engineering/public/app/select_item";
    private static String url_add_info = "http://203.160.94.175/ZH_Engineering/public/app/add_info_report";
    private static String url_add_details = "http://203.160.94.175/ZH_Engineering/public/app/add_details_report";
    private static String url_update_info = "http://203.160.94.175/ZH_Engineering/public/app/update_info_report";
    private static String url_update_details = "http://203.160.94.175/ZH_Engineering/public/app/update_details_report";

    private static String url_delete_report_type = "http://203.160.94.175/ZH_Engineering/public/app/delete_reportType";
    private static String url_add_report_type = "";
    private static String url_calendar_get_report = "http://203.160.94.175/ZH_Engineering/public/app/get_calender";

    public static String login(String name, String password) {
        String url = url_login + "?emp=" + name + "&pwd=" + password;
        return execute(url);
    }


    public static String getSearchList(String reportId) {
        //查询全部传  ""
        if (reportId == null) {
            reportId = "";
        }
        String url = url_get_search + "?reportID=" + reportId;
        return execute(url);
    }

    public static String delSearchList(String reportId) {
        //查询全部传  ""
        String url = url_del_search + "?reportID=" + reportId;
        return execute(url);
    }

    public static String getInfo(String reportId) {
        String url = url_get_info + "?reportID=" + reportId;
        return execute(url);
    }

    public static String getDetails(String reportId, String reportType) {
        String url = url_get_details + "?reportID=" + reportId + "&reportType=" + reportType;
        return execute(url);
    }

    private static String loadDataFromMem(String key) {
        //判断是否存在缓存
        if (cacheData.containsKey(key)) {
            String memData = cacheData.get(key);
            LogUtils.e("数据从内存中获取-->" + memData);
            return memData;
        }
        return null;
    }

    private static void writeData2Mem(String key, String value) {
        cacheData.put(key, value);
        LogUtils.e("保存数据到内存中-->" + value);
    }

    public static String getSelectItem() {
        String url = url_get_select_item;
        String result = loadDataFromMem(url);
        if (TextUtils.isEmpty(result)) {
            result = execute(url);
            if (!TextUtils.isEmpty(result)) {
                writeData2Mem(url,result);
            }
        }
        return result;
    }

    public static String uploadAddGet(String id, String date, String hour1, String hour2, String weather1, String weather2, String area) {
        //{"reportID":"xxx”,"reportDate":"xxx","dayTime":"xxx","nightTime":"xxx","dayWeather":"xxx","nightWeather":"xxx","area":"xxx","reportType":"[]"}
        String url = url_add_info + "?reportID=" + id
                + "&reportDate=" + date
                + "&dayTime=" + hour1
                + "&nightTime=" + hour2
                + "&dayWeather=" + weather1
                + "&nightWeather=" + weather2
                + "&area=" + area
                + "&reportType=" + "";
        return execute(url);
    }

    public static String addInfo(String json) {
        String url = url_add_info;
        return executePost(url, json);
    }

    public static String updateInfoPost(String json) {
        String url = url_update_info;
        return executePost(url, json);
    }

    public static String addDetails(String json) {
        String url = url_add_details;
        return executePost(url, json);
    }

    public static String updateDetails(HashMap<String, Object> map) {
        String url = url_update_details;
        return uploadImage(url, map);
    }

    private static String execute(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        LogUtils.e(url);
        switch_log_interceptor();
        try (Response response = mBuilder.build().newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            LogUtils.e("网络发生错误");
            return null;
        }
    }

    private static String executePost(String url, String json) {
        switch_log_interceptor();
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .post(body)
                .addHeader("Content-Type", "application/json")
                .url(url)
                .build();
        LogUtils.e("post = "+url);
        try (Response response = mBuilder.build().newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            LogUtils.e("网络发生错误");
            return null;
        }
    }

    public static String deleteReportType(int id, String reportType) {
        String url = url_delete_report_type + "?" + "srid=" + id + "&reportType=" + reportType;
        return execute(url);
    }

    public static String addReportType(int id, String reportType) {
        String url = url_add_report_type + "?" + "srid=" + id + "&reportType=" + reportType;
        return execute(url);
    }

    public static String calendarGetData(String reportID) {
        String url = url_calendar_get_report + "?reportDate=" + reportID;
        return execute(url);
    }

    private static String uploadImage(String url, HashMap<String, Object> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        switch_log_interceptor();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        MediaType mediaType = MediaType.parse("image/jpeg");
        // 把文件封装进请求体
        builder.setType(MultipartBody.FORM);
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {
                RequestBody fileBody = RequestBody.create(mediaType, (File) value);
                builder.addFormDataPart(key, ((File) value).getName(), fileBody);
            } else {
                builder.addFormDataPart(key, value.toString());
            }
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
//                .addHeader("Content-Type", "multipart/form-data")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0")
                .post(body)
                .build();
        LogUtils.e(request);
        Call call = mBuilder.build().newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
            e.printStackTrace();
        }
        return null;
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    System.out.println(response.body().string());
//                } else {
//                    System.out.println(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println(e.getMessage());
//            }
//        });
    }

    public static void switch_log_interceptor() {
        if (BuildConfig.DEBUG && !is_add_logInterceptor) {
            logInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            mBuilder.addNetworkInterceptor(logInterceptor);
            is_add_logInterceptor = true;
        }
    }

    public static byte[] getPicture(String pic_url) {
        Request request = new Request.Builder()
                .url(pic_url)
                .build();
        LogUtils.e(pic_url);
        switch_log_interceptor();
        try (Response response = mBuilder.build().newCall(request).execute()) {
            return response.body().bytes();
        } catch (Exception e) {
            LogUtils.e("网络发生错误"+e.getMessage());
            return null;
        }
    }
}
