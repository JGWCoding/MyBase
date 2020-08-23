package com.example.zw_engineering.ui.clp_fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;

import java.util.ArrayList;
import java.util.List;

public class LabourFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_labour_fragment2, container, false);
        initRecycler(root);
        return root;
    }

    private void initRecycler(View root) {
        RecyclerView recycler1 = root.findViewById(R.id.laboru2_recyler1);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler1.setAdapter(new DemoAdapter(list));
        RecyclerView recycler2 = root.findViewById(R.id.laboru2_recyler2);
        recycler2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler2.setAdapter(new DemoAdapter(list));
    }
    public class DemoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public DemoAdapter(List<String> list) {
            super(R.layout.layout_recycler_labour2, list);
        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.title, "Labour Type");
        }
    }

}
