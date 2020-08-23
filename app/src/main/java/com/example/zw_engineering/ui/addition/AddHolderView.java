package com.example.zw_engineering.ui.addition;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
import com.example.zw_engineering.base.BaseHolder;
import com.example.zw_engineering.ui.CLPFramgent;
import com.example.zw_engineering.ui.view.TimeRangePickerDialog;
import com.example.zw_engineering.util.ConstantUtil;
import com.example.zw_engineering.util.MyFragmentUtils;
import com.example.zw_engineering.util.MyNetWorkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddHolderView extends BaseHolder<String> {
    TextView view_date;
    TextView mView_hour1;
    TextView mView_hour2;
    TextView mView_weather1;
    TextView mView_weather2;
    EditText mView_report_type;
    RecyclerView mView_recycler;
    EditText mView_area;
    TimePickerView mPvTime;
    TimeRangePickerDialog mTimeDialog;
    OptionsPickerView pvOptions;
    ArrayList<String> mAdd_items;
    DemoAdapter mAdapter;
    View mView_add;
    ImageView mView_save;
    //条件选择器
     List<String> strings ;
    boolean clp_clickable;
    boolean is_add;
    int return_id;
    String reportID;
    View mView_calendar;

    public AddHolderView(LayoutInflater inflater, @Nullable ViewGroup container) {
        super(inflater, container, null);
    }

    @Override
    protected View initHolderView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addition,container,false);
    }

    @Override
    public View initHolderView() {
        return View.inflate(ActivityUtils.getTopActivity(), R.layout.fragment_addition, null);
    }

    @Override
    protected void initView(View root) {
        view_date = root.findViewById(R.id.add_fragment_date);
        mView_hour1 = root.findViewById(R.id.add_fragment_hour1);
        mView_hour2 = root.findViewById(R.id.add_fragment_hour2);
        mView_report_type = root.findViewById(R.id.add_fragment_report_type);
        mView_recycler = root.findViewById(R.id.add_fragment_recycler);
        mView_weather1 = root.findViewById(R.id.add_fragment_weather1);
        mView_weather2 = root.findViewById(R.id.add_fragment_weather2);
        mView_area = root.findViewById(R.id.add_fragment_area);
        mView_add = root.findViewById(R.id.add_fragment_add);
        mView_save = root.findViewById(R.id.add_fragment_save);
        mPvTime = new TimePickerBuilder(ActivityUtils.getTopActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String date2String = TimeUtils.date2String(date, "yyyy-MM-dd");
                view_date.setText(date2String);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        mTimeDialog = new TimeRangePickerDialog(ActivityUtils.getTopActivity(), "07:00 - 09:00", new TimeRangePickerDialog.ConfirmAction() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick(View v, String startAndEndTime) {
                if (v != null && v instanceof TextView) {
                    ((TextView) v).setText(startAndEndTime);
                }
            }
        });
        pvOptions = new OptionsPickerBuilder(ActivityUtils.getTopActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                ((TextView) v).setText(strings.get(options1));
            }
        }).build();
        strings = Arrays.asList(new String[]{"Fine", "Cloudy", "Shower", "Rainy", "Storm"});
        pvOptions.setPicker(strings);

        String date = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd");
        view_date.setText(date);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPvTime.show();
            }
        };
        mView_calendar = root.findViewById(R.id.add_fragment_calendar);
        mView_calendar.setOnClickListener(onClickListener);
        view_date.setOnClickListener(onClickListener);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeDialog.show(v);
            }
        };
        mView_hour1.setOnClickListener(onClickListener);
        mView_hour2.setOnClickListener(onClickListener);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show(v);
            }
        };
        mView_weather1.setOnClickListener(onClickListener);
        mView_weather2.setOnClickListener(onClickListener);
        mView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mView_report_type.getText())) {
                    String text = mView_report_type.getText().toString();
                    if (mAdd_items.contains(text)) {
                        ToastUtils.showShort(text + " is add ");
                    } else {
                        clp_clickable = false;
                        mAdd_items.add(0, text);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        mView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        mView_recycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mAdd_items = new ArrayList<>();
        mAdapter = new DemoAdapter(mAdd_items);
        mView_recycler.setAdapter(mAdapter);
    }

    private void upload() {
        final String json = getUploadJson();
        if (TextUtils.isEmpty(json)) {
            ToastUtils.showShort("json data is error");
        } else {
            LogUtils.e(json);
            ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
                @Override
                public String doInBackground() throws Throwable {
                    if (TextUtils.isEmpty(reportID)){
                        is_add = true;
                        return MyNetWorkUtils.addInfo(json);
                    }else {
                        is_add = false;
                        return MyNetWorkUtils.updateInfoPost(json);
                    }
                }

                @Override
                public void onFail(Throwable t) {
                    ToastUtils.showLong("do fail");
                }

                @Override
                public void onSuccess(String result) {
                    if (JsonUtils.getInt(result, "code") == 200) {
                        try {
                            //{"code":200,"reportID":"20200316_14","id":148}
                            return_id = new JSONObject(result).getInt("id"); //todo 这里后台返回模式错误
                            reportID = new JSONObject(result).getString("reportID");
                            if (is_add){
                                ConstantUtil.last_report_id = reportID;
                                SPUtils.getInstance().put(ConstantUtil.add_reportID_key, reportID, true);
                            }
                            LogUtils.e( ConstantUtil.last_report_id);
                        } catch (JSONException e) {
                            LogUtils.e(e.getMessage());
                        }
                        clp_clickable = true;
                        ToastUtils.showLong(is_add?"add success":"update success");
                    } else {
                        ToastUtils.showLong(is_add?"add fail":"update fail");
                    }
                    LogUtils.e(result);
                }
            });
        }
    }

    private String getUploadJson() {
        String date = getViewText(view_date);
        String hour1 = getViewText(mView_hour1);
        String hour2 = getViewText(mView_hour2);
        String weather1 = getViewText(mView_weather1);
        String weather2 = getViewText(mView_weather2);
        String area = getViewText(mView_area);
        JSONObject jo = new JSONObject();
        try {
            if (TextUtils.isEmpty(reportID)) {
                jo.put("reportID", date.replaceAll("-", ""));
            } else {
                jo.put("reportID", reportID);
            }
            jo.put("reportDate", date);
            jo.put("dayTime", hour1);
            jo.put("nightTime", hour2);
            jo.put("dayWeather", weather1);
            jo.put("nightWeather", weather2);
            jo.put("area", area);
            JSONArray reportType = new JSONArray();
            for (int i = 0; i < mAdd_items.size(); i++) {
                reportType.put(mAdd_items.get(i));
            }
            jo.put("reportType", reportType);
            return jo.toString();
            //{"reportID":"xxx”,"reportDate":"xxx","dayTime":"xxx","nightTime":"xxx","dayWeather":"xxx","nightWeather":"xxx","area":"xxx","reportType":"[]"}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getViewText(TextView tv) {
        if (TextUtils.isEmpty(tv.getText())) {
            return tv.getHint().toString();
        } else {
            return tv.getText().toString();
        }
    }

    @Override
    public void refreshHolderView(String data) {
        parseData(data);
    }

    private void parseData(String result) {
        LogUtils.e(result);
        if (TextUtils.isEmpty(result)) {
            ToastUtils.showLong("get data fail");
        } else {
            int code = JsonUtils.getInt(result, "code");
            if (code == 200) {
                //{"code":200,"data":{"id":2,"reportID":"15\/02\/2020","reportDate":null,"dayTime":null,"nightTime":null,"dayWeather":null,"nightWeather":null,"area":null,"status":1,"lock_edit":"2020-02-13 15:19:34","checker":null,"created_at":"2020-02-13 15:19:34","updated_at":"2020-02-14 17:14:26"}}
                JSONObject jsonObject = JsonUtils.getJSONObject(result, "data", null);
                if (jsonObject != null) {
                try {
                    reportID = jsonObject.optString("reportID");
                    String text = reportID.substring(0, 8);
                    text = text.substring(0, 4) + "-" + text.substring(4, 6) + "-" + text.substring(6);
                    view_date.setText(text);
                    setText(mView_hour1, jsonObject.getString("dayTime"));
                    setText(mView_hour2, jsonObject.optString("nightTime"));
                    setText(mView_weather1, jsonObject.optString("dayWeather"));
                    setText(mView_weather2, jsonObject.optString("nightWeather"));
                    setText(mView_area, jsonObject.optString("area"));

//                    try {
//                        JSONArray reportType = jsonObject.getJSONArray("reportType");   //todo add接口解析reportType,这里由于有两个接口,返回不一样位置
//                        if (TextUtils.isEmpty(reportType.toString()) == false && reportType.length() > 0) {
//                            mAdd_items.clear();
//                            for (int i = 0; i < reportType.length(); i++) {
//                                mAdd_items.add((String) reportType.get(i));
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        }
//                        LogUtils.e(mAdd_items.size());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        LogUtils.e(e.getMessage());
//                    }

                        return_id = jsonObject.getInt("id");
                        JSONArray reportType = new JSONObject(result).getJSONArray("reportType");
                        if (TextUtils.isEmpty(reportType.toString()) == false && reportType.length() > 0) {
                            mAdd_items.clear();
                            for (int i = 0; i < reportType.length(); i++) {
                                mAdd_items.add((String) reportType.get(i));
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                    }
                }
            } else {
                ToastUtils.showLong("get data fail");
            }
        }
    }
    @Override
    public void setText(TextView view, String text) {
        if (view!=null && !TextUtils.isEmpty(text)) {
            view.setHint(text);
        }
        LogUtils.e(text,text==null,"null".equals(text));
    }
    public class DemoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public DemoAdapter(List<String> list) {
            super(R.layout.addition_recycler_item, list);
        }
        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(final BaseViewHolder helper, final String item) {
            helper.setText(R.id.add_recycler_text, item);
            helper.setOnClickListener(R.id.add_recycler_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clp_clickable) {
                        MyFragmentUtils.addNotMain(((AppCompatActivity) ActivityUtils.getTopActivity()).getSupportFragmentManager(), CLPFramgent.getInstance(item, return_id, reportID), R.id.nav_host_fragment, false, true);
                    }else {
                        ToastUtils.showShort("please save");
                    }
                }
            });
            helper.setOnClickListener(R.id.add_recycler_del, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(helper.getPosition());
                }
            });
        }
    }
}
