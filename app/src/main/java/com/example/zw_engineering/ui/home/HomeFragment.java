package com.example.zw_engineering.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
import com.example.zw_engineering.base.BaseFragment;
import com.example.zw_engineering.base.LoadingPager;
import com.example.zw_engineering.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends BaseFragment {


    private RecyclerView recycler;
    private TextView view_title;
    private boolean completed = false;
    ArrayList<HomeBean> list = new ArrayList<HomeBean>();
    private DemoAdapter adapter;

    @Override
    public LoadingPager.LoadedResult initData() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 20; i++) {
            list.add(new HomeBean());
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }

    @Override
    public View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        recycler = root.findViewById(R.id.home_fragment_recycler);
        view_title = root.findViewById(R.id.home_fragment_title);
        adapter = new DemoAdapter(list);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
        view_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completed = !completed;
                if (completed) {
                    view_title.setText("Report Completed");
                    view_title.setTextColor(getResources().getColor(R.color.red));
                    //todo 需要请求接口   拿数据
                    adapter.notifyDataSetChanged();
                } else {
                    view_title.setText("Report Incompleted");
                    view_title.setTextColor(getResources().getColor(R.color.yellow));
                    //todo 需要请求接口拿数据
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    public class DemoAdapter extends BaseQuickAdapter<HomeBean, BaseViewHolder> {
        public DemoAdapter(List<HomeBean> list) {
            super(R.layout.recycler_home, list);
        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(final BaseViewHolder helper, final HomeBean item) {
            Button view = helper.getView(R.id.home_item_view);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (completed) {
                view.setText("Check");
                view.setBackgroundColor(getResources().getColor(R.color.red));
                parent.setBackground(getResources().getDrawable(R.drawable.home_item_red));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("go to check");
                    }
                });
            } else {
                view.setText("View");
                view.setBackgroundColor(getResources().getColor(R.color.yellow));
                parent.setBackground(getResources().getDrawable(R.drawable.home_item_yellow));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("go to view");
//                        ReadAdditionFragment.reportID = ConstantUtil.last_report_id; //todo 需要请求接口拿数据
//                        MyFragmentUtils.addNotMain(getActivity().getSupportFragmentManager(), new ReadAdditionFragment(), R.id.nav_host_fragment, false, true);
                    }
                });
            }
        }
    }
}