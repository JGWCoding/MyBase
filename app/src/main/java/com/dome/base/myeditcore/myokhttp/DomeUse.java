package com.dome.base.myeditcore.myokhttp;

import android.util.Log;

public class DomeUse {
    HttpClient client;

    protected void dome() {

        client = new HttpClient.Builder()
                .setRetryTimes(3)
                .build();
    }

    public void get() {
        Request request = new Request.Builder()
                .setHttpUrl("http://www.kuaidi100.com/query?type=yuantong&postid=222222222")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("响应体", response.getBody());
            }
        });
    }

    public void post() {
        RequestBody body = new RequestBody()
                .add("key", "064a7778b8389441e30f91b8a60c9b23")
                .add("city", "深圳");


        Request request = new Request.Builder()
                .setHttpUrl("http://restapi.amap.com/v3/weather/weatherInfo")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("响应体", response.getBody());
            }
        });
    }
}
