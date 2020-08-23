package com.example.zw_engineering.ui.addition;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
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

public class AdditionFragment_copy extends Fragment {
    public static String reportID;
    OptionsPickerView pvOptions;
    TextView view_date;
    private TextView mView_hour1;
    private TextView mView_hour2;
    private TimePickerView mPvTime;
    private TextView mView_weather1;
    private TextView mView_weather2;
    private EditText mView_report_type;
    private RecyclerView mView_recycler;
    private ArrayList<String> mAdd_items;
    private DemoAdapter mAdapter;
    private EditText mView_area;
    private TimeRangePickerDialog mTimeDialog;
    private boolean clp_clickable;
    private int return_id = -1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addition, container, false);
        initView(root);
        setInfo();
        return root;
    }

    private void initView(View root) {
        view_date = root.findViewById(R.id.add_fragment_date);
        String date = TimeUtils.millis2String(System.currentTimeMillis(), "yyyy-MM-dd");
        view_date.setText(date);
        mView_hour1 = root.findViewById(R.id.add_fragment_hour1);
        mView_hour2 = root.findViewById(R.id.add_fragment_hour2);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPvTime.show();
            }
        };
        root.findViewById(R.id.add_fragment_calendar).setOnClickListener(onClickListener);
        view_date.setOnClickListener(onClickListener);
        mPvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String date2String = TimeUtils.date2String(date, "yyyy-MM-dd");
                view_date.setText(date2String);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();

        mTimeDialog = new TimeRangePickerDialog(getActivity(), "07:00 - 09:00", new TimeRangePickerDialog.ConfirmAction() {
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

        mView_hour1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeDialog.show(v);
            }
        });
        mView_hour2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeDialog.show(v);
            }
        });
        mView_weather1 = root.findViewById(R.id.add_fragment_weather1);
        mView_weather2 = root.findViewById(R.id.add_fragment_weather2);
        //条件选择器
        final List<String> strings = Arrays.asList(new String[]{"Fine", "Cloudy", "Shower", "Rainy", "Storm"});
        pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                ((TextView) v).setText(strings.get(options1));
            }
        }).build();
        pvOptions.setPicker(strings);
        mView_weather1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show(v);
            }
        });
        mView_weather2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show(v);
            }
        });
//        root.findViewById(R.id.add_fragment_clp).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyFragmentUtils.addNotMain(getActivity().getSupportFragmentManager(),new CLPFramgent(),R.id.nav_host_fragment,false,true);
//            }
//        });
        mView_report_type = root.findViewById(R.id.add_fragment_report_type);
        mView_recycler = root.findViewById(R.id.add_fragment_recycler);
        mView_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdd_items = new ArrayList<>();
//        mAdd_items.add("CLP Ducting Works");
//        mAdd_items.add("Communication System Ducting Works");
//        mAdd_items.add("Miscellaneous Works(Survey/Trail Pit/Underground Utilities sadfasdfasdfasd)");
        mAdapter = new DemoAdapter(mAdd_items);
        mView_recycler.setAdapter(mAdapter);
        root.findViewById(R.id.add_fragment_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mView_report_type.getText())) {
                    mAdd_items.add(0, mView_report_type.getText().toString());
                    mAdapter.notifyDataSetChanged();
//                    mAdapter.addData(0,mView_report_type.getText().toString());
                }
            }
        });
        mView_area = root.findViewById(R.id.add_fragment_area);
        root.findViewById(R.id.add_fragment_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String date = view_date.getText().toString();
                final String hour1 = mView_hour1.getText().toString();
                final String hour2 = mView_hour2.getText().toString();
                final String weather1 = mView_weather1.getText().toString();
                final String weather2 = mView_weather2.getText().toString();
                final String area = mView_area.getText().toString();
                final JSONObject jo = new JSONObject();
                try {
                    jo.put("reportID", date.replaceAll("-",""));
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
                    jo.put("reportType",reportType);
                    //{"reportID":"xxx”,"reportDate":"xxx","dayTime":"xxx","nightTime":"xxx","dayWeather":"xxx","nightWeather":"xxx","area":"xxx","reportType":"[]"}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
                    @Override
                    public String doInBackground() throws Throwable {
                        LogUtils.e(jo.toString());
//                        return MyNetWorkUtils.uploadAddGet(date, date, hour1, hour2, weather1, weather2, area);


                        return MyNetWorkUtils.addInfo(jo.toString());
                    }

                    @Override
                    public void onSuccess(String result) {
                        if (JsonUtils.getInt(result, "code") == 200) {
                            try {
                                return_id  = new JSONObject(result).getJSONObject("msg").getInt("id");
                                reportID = new JSONObject(result).getJSONObject("msg").getString("reportID");
                                ConstantUtil.last_report_id = reportID;
//                                return_id = JsonUtils.getJSONObject(result,"msg",new JSONObject("\"id\":-1")).getInt("id");
                                SPUtils.getInstance().put(ConstantUtil.add_reportID_key,reportID,true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            clp_clickable = true;
                            ToastUtils.showLong("add success");
                        } else {
                            ToastUtils.showLong("add fail");
                        }
                        LogUtils.e("return_id="+return_id+result);
                    }
                });
            }
        });
    }

    private void setInfo() {
        if (!TextUtils.isEmpty(reportID)) {
            ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
                @Override
                public String doInBackground() throws Throwable {
                    return MyNetWorkUtils.getInfo(reportID);
                }

                @Override
                public void onSuccess(String result) {
                    parseData(result);
                }
            });
        }
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
                    setText(view_date,jsonObject.optString("reportID").substring(0,10));
                    setText(mView_hour1,jsonObject.optString("dayTime"));
                    setText(mView_hour2,jsonObject.optString("nightTime"));
                    setText(mView_weather1,jsonObject.optString("dayWeather"));
                    setText(mView_weather2,jsonObject.optString("nightWeather"));
                    setText(mView_area,jsonObject.optString("area"));
                    try {
                        JSONArray reportType = jsonObject.getJSONArray("reportType");
                        if (TextUtils.isEmpty(reportType.toString())==false&&reportType.length()>0){
                            mAdd_items.clear();
                            for (int i = 0; i < reportType.length(); i++) {
                                mAdd_items.add((String) reportType.get(i));
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //TODO 这里设置补全信息

                }

            } else {
                ToastUtils.showLong("get data fail");
            }
        }
    }

    private void setText(TextView view, String info) {
        if (!TextUtils.isEmpty(info)){
            view.setText(info);
        }
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
                        MyFragmentUtils.addNotMain(getActivity().getSupportFragmentManager(), CLPFramgent.getInstance(item,return_id,reportID), R.id.nav_host_fragment, false, true);
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