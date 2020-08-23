package com.example.zw_engineering.ui.clp_fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
import com.example.zw_engineering.util.MyNetWorkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LabourFragment1 extends Fragment {
    ArrayList<String> list_select = new ArrayList<String>();
    OptionsPickerView pvOptions;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_labour1, container, false);
        init(root);
        return root;
    }

    private void init(final View root) {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.getSelectItem();
            }

            @Override
            public void onSuccess(String result) {
//{"code":200,"data":["Foreman","Ganger","Concreter","Operator","M\/Labour","Carpenter","Steel Fixer","Rigger","Fitler","Welder","Scaffolder","Site Agent","Surveyor","Chainman","Driver","F\/Labour","Technician","Electrician","Storekeeper","L\/O Man","Watchman","Diver"]}
                if (JsonUtils.getInt(result, "code") == 200 && !TextUtils.isEmpty(result)) {
                    JSONArray data = JsonUtils.getJSONArray(result, "data", null);
                    if (data != null && TextUtils.isEmpty(data.toString()) == false) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                list_select.add(data.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                initRecycler(root);
            }
        });

    }

    private void initRecycler(View root) {
        pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                ((TextView)v).setText(list_select.get(options1));
            }
        }).build();
        pvOptions.setPicker(list_select);

        RecyclerView recycler1 = root.findViewById(R.id.laboru1_recyler1);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler1.setAdapter(new DemoAdapter(list));
        RecyclerView recycler2 = root.findViewById(R.id.laboru1_recyler2);
        recycler2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler2.setAdapter(new DemoAdapter(list));
    }

    public class DemoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public DemoAdapter(List<String> list) {
            super(R.layout.layout_recycler_labour1, list);
        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(final BaseViewHolder helper, String item) {
            helper.setText(R.id.title, "Labour Type");
            helper.setOnClickListener(R.id.title, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pvOptions.show(v);
                }
            });
            helper.setOnClickListener(R.id.del, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(helper.getPosition());
                }
            });
        }
    }

}
