package com.example.zw_engineering.ui.calendar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
import com.example.zw_engineering.base.BaseFragment;
import com.example.zw_engineering.base.LoadingPager;
import com.example.zw_engineering.bean.CalendarBean;
import com.example.zw_engineering.util.MyNetWorkManager;
import com.example.zw_engineering.util.MyNetWorkUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarFragment extends BaseFragment {


    private ArrayList<CalendarBean> mList = new ArrayList<>();
    private RecyclerView mRecycler;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
//        initView(root);
//        Calendar calendar = Calendar.getInstance();
//        String reportID = getReportID(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
//        getNetWorkData(reportID);
//        return root;
//    }

    @Override
    public LoadingPager.LoadedResult initData() {
        Calendar calendar = Calendar.getInstance();
        String reportID = getReportID(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        String result = MyNetWorkUtils.calendarGetData(reportID);
        if (TextUtils.isEmpty(result) == false && JsonUtils.getInt(result, "code", 0) == 200) {
            JSONArray data = JsonUtils.getJSONArray(result, "data", null);
            if (data != null) {
                mList.clear();
                for (int i = 0; i < data.length(); i++) {
                    try {
                        CalendarBean calendarBean = GsonUtils.fromJson(data.get(i).toString(), CalendarBean.class);
                        mList.add(calendarBean);
                    } catch (JSONException e) {
                        LogUtils.e(e.getMessage());
                        return LoadingPager.LoadedResult.ERROR;
                    }
                }
                ToastUtils.showShort("request success");
            }
        }else {
            return LoadingPager.LoadedResult.EMPTY;
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }

    @Override
    public View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        CalendarView view_calendar = root.findViewById(R.id.calendar_fragment_calendarView);
        view_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String text = getReportID(year, month + 1, dayOfMonth);
                getNetWorkData(text);
            }
        });
        mRecycler = root.findViewById(R.id.calendar_fragment_recyler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(new DemoAdapter(mList));
    }

    @NotNull
    private String getReportID(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");
        if (month < 10) {
            sb.append(0).append(month).append("-");
        } else {
            sb.append(month).append("-");
        }
        if (day < 10) {
            sb.append(0).append(day);
        } else {
            sb.append(day);
        }
        return sb.toString();
    }

    public void getNetWorkData(String reportID) {
        MyNetWorkManager.calendarGetReportIDData(reportID, mList, new Runnable() {
            @Override
            public void run() {
                if (mRecycler != null && mRecycler.getAdapter() != null){
                    mRecycler.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    public class DemoAdapter extends BaseQuickAdapter<CalendarBean, BaseViewHolder> {

        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public DemoAdapter(List<CalendarBean> list) {
            super(R.layout.layout_recycler_calendar, list);
        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(BaseViewHolder helper, CalendarBean item) {
//            helper.setText(R.id.calendar_item_report_type, item.reportID);
            setText((TextView) helper.getView(R.id.calendar_item_dayHour), item.dayTime);
            setText((TextView) helper.getView(R.id.calendar_item_nightHour), item.nightTime);
            setText((TextView) helper.getView(R.id.calendar_item_area), item.area);
        }

        private void setText(TextView view, String text) {
            if (view != null && TextUtils.isEmpty(text) == false && !"null".equals(text.toLowerCase())) {
                view.setText(text);
            }
        }
    }
}