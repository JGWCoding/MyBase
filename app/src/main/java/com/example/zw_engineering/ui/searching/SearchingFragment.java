package com.example.zw_engineering.ui.searching;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
import com.example.zw_engineering.base.BaseFragment;
import com.example.zw_engineering.base.LoadingPager;
import com.example.zw_engineering.bean.SearchBean;
import com.example.zw_engineering.ui.addition.ReadAdditionFragment;
import com.example.zw_engineering.ui.view.RecycleViewDivider;
import com.example.zw_engineering.ui.view.SwipeItemLayout;
import com.example.zw_engineering.util.ConstantUtil;
import com.example.zw_engineering.util.MyFragmentUtils;
import com.example.zw_engineering.util.MyNetWorkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SearchingFragment extends BaseFragment {

    private RecyclerView mRecycle;
    EditText search;
    ArrayList<SearchBean> list = new ArrayList<>();
    SwipeRefreshLayout refresh;
    private Button mSearch_btn;


    @Override
    public View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_searching, container, false);
        mRecycle = root.findViewById(R.id.search_fragment_recycle);
        refresh = root.findViewById(R.id.search_fragment_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search.setText("");
                searchIDNet("");
            }
        });
        search = root.findViewById(R.id.search_activity_date1);
        mSearch_btn = root.findViewById(R.id.search_fragment_btn);
        mSearch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = search.getText().toString();
                searchIDNet(text);
            }
        });
        initRecycle();
        return root;
    }

    @Override
    public LoadingPager.LoadedResult initData() {
        final String searchList = MyNetWorkUtils.getSearchList("");
        int code = JsonUtils.getInt(searchList, "code");
        LogUtils.e(searchList);
        if (code == 200) {
            Utils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parseData(searchList);
                }
            });
        } else {
            return LoadingPager.LoadedResult.ERROR;
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }

    void loadDataView() {
        refresh.setRefreshing(true);
    }

    void loadDataViewEnd() {
        if (refresh != null) {
            refresh.setRefreshing(false);
        }
    }

    private void searchIDNet(final String text) {
        loadDataView();
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.getSearchList(text);
            }

            @Override
            public void onFail(Throwable t) {
                loadDataViewEnd();
            }

            @Override
            public void onSuccess(String result) {
                parseData(result);
            }
        });
    }

    private void parseData(String result) {
        LogUtils.e(result);
        if (TextUtils.isEmpty(result)) {
            ToastUtils.showLong("get data fail");
        } else {
            int code = JsonUtils.getInt(result, "code");
            if (code == 200) {
                //{"code":200,"data":[{"id":1,"reportID":"123","status":1},{"id":2,"reportID":"1234","status":1}]}
                JSONArray jsonArray = JsonUtils.getJSONArray(result, "data", null);
                if (jsonArray != null) {
                    list.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            String json = jsonArray.getJSONObject(i).toString();
                            SearchBean bean = GsonUtils.fromJson(json, SearchBean.class);
                            list.add(bean);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showLong("sever data is error");
                        }
                    }
                    ConstantUtil.search_list = list;
                    if (mRecycle != null && mRecycle.getAdapter() != null) {
                        mRecycle.getAdapter().notifyDataSetChanged();
                    }

                }
            } else {
                ToastUtils.showLong("get data fail");
            }
        }
        loadDataViewEnd();
    }


    private void initRecycle() {
        DemoAdapter adapter = new DemoAdapter(list);
        mRecycle.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        mRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycle.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getActivity()));
        mRecycle.setAdapter(adapter);
        adapter.setEmptyView(R.layout.pager_empty, mRecycle);
    }

    public class DemoAdapter extends BaseQuickAdapter<SearchBean, BaseViewHolder> {

        public DemoAdapter(List<SearchBean> list) {
            super(R.layout.recycler_swipe, list);
        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(final BaseViewHolder helper, final SearchBean item) {
            helper.setText(R.id.form_date, item.reportID);
            int status = item.status;
//            1显示  Incomplete
//            2显示 Checking
//            3显示 Finished
            helper.setText(R.id.form_time, status==1?"Incomplete":(status==2?"Checking":"Finished"));    //status 数字 代表什么状态
            helper.getView(R.id.view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入界面查看数据
                    ReadAdditionFragment.reportID = item.reportID;
                    MyFragmentUtils.addNotMain(getActivity().getSupportFragmentManager(), new ReadAdditionFragment(), R.id.nav_host_fragment, false, true);
                }
            });
            helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 删除自身  网络删除
                    remove(mData.indexOf(item));
                    ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
                        @Override
                        public Object doInBackground() throws Throwable {
//                            HttpUtils.delete(item.id);
                            MyNetWorkUtils.delSearchList(item.reportID);
                            return null;
                        }
                        @Override
                        public void onSuccess(Object result) {
                            list.remove(item);
                        }

                        @Override
                        public void onFail(Throwable t) {
                            ToastUtils.showLong("delete reportID " + item.reportID + " is fail");
                        }
                    });
                }
            });

        }
    }
}