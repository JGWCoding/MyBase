package com.example.zw_engineering.ui.clp_fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
import com.example.zw_engineering.bean.AppSelectBean;
import com.example.zw_engineering.bean.LabourBean;
import com.example.zw_engineering.listener.MyTextChangedListener;
import com.example.zw_engineering.util.ConstantUtil;
import com.example.zw_engineering.util.MyNetWorkUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LabourFragment extends Fragment {
    ArrayList<String> select_add_item_list = new ArrayList();     //选择器    add Item 的选择器 内容
    ArrayList<String> select_labour_type_list = new ArrayList();    //选择器   装labour type选择器 内容
    public ArrayList<String> item_title_list = new ArrayList();   // 装  item 数据 title 数据的
    public ArrayList<ArrayList<LabourBean>> item_childs_list = new ArrayList<>();  //装最小的recycler条目数据的
    OptionsPickerView pvOptions;
    public RecyclerView mRecycler;
    private LabourBean mLabourBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_labour, container, false);
        init(root);
        initRecycler(root);
        return root;
    }

    private void init(final View root) {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.getSelectItem();
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                initRecycler(root);
            }

            @Override
            public void onSuccess(String result) {
//{"code":200,"data":["Foreman","Ganger","Concreter","Operator","M\/Labour","Carpenter","Steel Fixer","Rigger","Fitler","Welder","Scaffolder","Site Agent","Surveyor","Chainman","Driver","F\/Labour","Technician","Electrician","Storekeeper","L\/O Man","Watchman","Diver"]}
                if (JsonUtils.getInt(result, "code") == 200 && !TextUtils.isEmpty(result)) {
                    AppSelectBean appSelectBean = GsonUtils.fromJson(result, AppSelectBean.class);
                    ConstantUtil.appSelectBean = appSelectBean;
                    for (int i = 0; i < ((appSelectBean == null || appSelectBean.labour_item == null) ? 0 : appSelectBean.labour_item.size()); i++) {
                        LogUtils.e(appSelectBean.toString());
                        AppSelectBean.LabourItemBean labourItemBean = appSelectBean.labour_item.get(i);
                        select_add_item_list.add(labourItemBean.name);
                    }
                }
//                    JSONArray data = JsonUtils.getJSONArray(result, "labour_item", null);
//                    if (data != null && TextUtils.isEmpty(data.toString()) == false) {
//                        for (int i = 0; i < data.length(); i++) {
//                            try {
//                                select_add_item_list.add(data.getString(i));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    data = JsonUtils.getJSONArray(result, "labour_type", null);
//                    if (data != null && TextUtils.isEmpty(data.toString()) == false) {
//                        for (int i = 0; i < data.length(); i++) {
//                            try {
//                                select_labour_type_list.add(data.getString(i));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
                initRecycler(root);
            }
        });

    }

    private void initRecycler(View root) {
        pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                try {
                    if (v.getId() == R.id.fragment_labour_add) {
                        item_title_list.add(select_add_item_list.get(options1));
                        ArrayList<LabourBean> list = new ArrayList<>();
                        list.add(new LabourBean(select_add_item_list.get(options1)));
                        list.add(new LabourBean(select_add_item_list.get(options1)));
                        item_childs_list.add(list);
//                    ((DemoAdapter) mRecycler.getAdapter()).addData(select_add_item_list.get(options1));
                        mRecycler.getAdapter().notifyDataSetChanged();
                        select_add_item_list.remove(options1);  //选项移除已经选了的
                    } else if (v.getId() == R.id.labour2_title) {
                        ((TextView) v).setText(select_labour_type_list.get(options1));
                        mLabourBean.title = select_labour_type_list.get(options1);
                        mLabourBean.title_isBlack = true;
                    }
                }catch (Exception e){
                    //这里有可能没有设置数据集 会有错
                    LogUtils.e(e.getMessage());
                }

            }
        }).isDialog(true).build();
        root.findViewById(R.id.fragment_labour_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断网络是否已经加载了 add Item
                for (int i = 0; i < item_title_list.size(); i++) {
                    if (select_add_item_list.contains(item_title_list.get(i))) {
                        select_add_item_list.remove(item_title_list.get(i));
                    }
                }
                pvOptions.setPicker(select_add_item_list);
                pvOptions.show(v);
            }
        });
        mRecycler = root.findViewById(R.id.fragment_labour_recyler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(new DemoAdapter(item_title_list));
    }

    public class DemoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public DemoAdapter(List<String> list) {
            super(R.layout.recycler_labour, list);
        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            helper.setIsRecyclable(false); //recycler嵌套recycler   view复用有问题
//            LogUtils.e("加载" + item);
            helper.setText(R.id.labour_recycler_text, item);
            final RecyclerView recyclerView = helper.getView(R.id.labour_recycler_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            final ArrayList<LabourBean> list = item_childs_list.get(helper.getPosition());
            recyclerView.setAdapter(new ChildAdapter(list));
            helper.setOnClickListener(R.id.labour_recycler_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.add(new LabourBean(item));
                    recyclerView.getAdapter().notifyDataSetChanged();
//                    ((ChildAdapter)recyclerView.getAdapter()).addData(new LabourBean());
                }
            });
        }
    }

    public class ChildAdapter extends BaseQuickAdapter<LabourBean, BaseViewHolder> {
        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public ChildAdapter(List<LabourBean> list) {
            super(R.layout.layout_recycler_labour2, list);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final LabourBean item) {
            helper.setIsRecyclable(false);  //禁止复用--有EditText.addTextChangedListener,复用时设置文本监听到
            int position = helper.getPosition();
            LogUtils.e("child加载" + position + item.toString());
            TextView title = (TextView) helper.getView(R.id.labour2_title);
            final EditText quantity = (EditText) helper.getView(R.id.labour2_quantity);
            final EditText owner = (EditText) helper.getView(R.id.labour2_owner);
            setText(title, item.title, item.title_isBlack);
            setText(quantity, item.quantity, item.quantity_isBlack);
            setText(owner, item.owner, item.owner_isBlack);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e(ConstantUtil.appSelectBean.toString());
                    for (int i = 0; i < ((ConstantUtil.appSelectBean==null||ConstantUtil.appSelectBean.labour_item==null)?0:ConstantUtil.appSelectBean.labour_item.size()); i++) {
                        if (ConstantUtil.appSelectBean.labour_item.get(i).name.equals(item.item)){
                            select_labour_type_list.clear();
                            int id = ConstantUtil.appSelectBean.labour_item.get(i).id;
                            for (int j = 0; j < ConstantUtil.appSelectBean.labour_type.size(); j++) {
                                AppSelectBean.LabourTypeBean typeBean = ConstantUtil.appSelectBean.labour_type.get(j);
                                if (typeBean.pid==id){
                                    select_labour_type_list.add(typeBean.name);
                                }
                            }
                            break;
                        }
                    }
                    LogUtils.e(select_labour_type_list.size());
                    pvOptions.setPicker(select_labour_type_list);
                    mLabourBean = item;
                    pvOptions.show(v);
                }
            });
            owner.addTextChangedListener(new MyTextChangedListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        item.owner = owner.getHint().toString();
                        item.owner_isBlack = false;
                    } else {
                        item.owner = s.toString();
                        item.owner_isBlack = true;
                    }
                }
            });
            quantity.addTextChangedListener(new MyTextChangedListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (TextUtils.isEmpty(s.toString())) {
                            String hint = quantity.getHint().toString();
                            if (!"Quantity".equals(hint)) {
                                Integer value = Integer.parseInt(hint);
                                item.quantity = value + "";
                                item.quantity_isBlack = false;
                            } else {
                                item.quantity = 0 + "";
                                item.quantity_isBlack = false;
                            }
                        } else {
                            Integer value = Integer.parseInt(s.toString());
                            item.quantity = value + "";
                            item.quantity_isBlack = true;
                        }

                    } catch (Exception e) {
                        item.quantity = 0 + "";
//                        ToastUtils.showLong("input error");
                        LogUtils.e(e.getMessage());
                    }
                }
            });

            final int childPosition = position;
            helper.setOnClickListener(R.id.labour2_recycler_del, new View.OnClickListener() {
                @Override
                public void onClick(View v) {   //这个一开始时加载删除时全删了
                    if (mData.size() == 1) {
                        int index = item_childs_list.indexOf(mData);
                        select_add_item_list.add(item_title_list.get(index));
                        item_childs_list.remove(index);
                        item_title_list.remove(index);
                        mRecycler.getAdapter().notifyDataSetChanged();
//                        //todo这里可能需要延时处理  (等视图刷新完成再可以点击)
//                        ((DemoAdapter)mRecycler.getAdapter()).remove(index);// TODO IndexOutOfBoundsException: Index: 3, Size: 3
                        return;
                    } else {
//                        remove(childPosition);
                        mData.remove(childPosition);
                        notifyDataSetChanged();
//                        LogUtils.e(childPosition + item.toString());
                    }
                }
            });
        }

        private void setText(TextView view, String text, boolean isBlack) {
//            if (TextUtils.isEmpty(text)==false&&"null".equals(text.toLowerCase())==false){
            if (!TextUtils.isEmpty(text)) {
                if (isBlack) {
                    view.setText(text);
                } else {
                    view.setText("");
                    view.setHint(text);
                }
            } else {
                view.setText("");
            }
        }
    }
}
