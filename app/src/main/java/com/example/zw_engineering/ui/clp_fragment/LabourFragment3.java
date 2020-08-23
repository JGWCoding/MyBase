package com.example.zw_engineering.ui.clp_fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;
import com.example.zw_engineering.bean.AppSelectBean;
import com.example.zw_engineering.bean.Labour3Bean;
import com.example.zw_engineering.listener.MyTextChangedListener;
import com.example.zw_engineering.util.ConstantUtil;
import com.example.zw_engineering.util.MyNetWorkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Plant Fragment
 * A simple {@link Fragment} subclass.
 */
public class LabourFragment3 extends Fragment {
    ArrayList<String> plant_item_list = new ArrayList<String>();
    ArrayList<String> plant_type_list = new ArrayList<String>();
    ArrayList<String> plant_model_list = new ArrayList<String>();
    OptionsPickerView pvOptions;
    public ArrayList<Labour3Bean> mList = new ArrayList<>();
    private DemoAdapter mAdapter;
    public RecyclerView mRecycler;
    private Labour3Bean mBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_labour_fragment3, container, false);
        init(root);
        return root;
    }

    private void init(final View root) {
        pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                try {
                    if (v.getId() == R.id.labour3_plant_item) {
                        mBean.title = plant_item_list.get(options1);
                        mBean.title_is_black = true;
                        ((TextView) v).setText(plant_item_list.get(options1));
                    } else if (v.getId() == R.id.labour3_plant_type) {
                        mBean.typeName = plant_type_list.get(options1);
                        mBean.typeName_is_black = true;
                        ((TextView) v).setText(plant_type_list.get(options1));
                    } else if (v.getId() == R.id.labour3_plant_model) {
                        mBean.model = plant_model_list.get(options1);
                        mBean.model_is_black = true;
                        ((TextView) v).setText(plant_model_list.get(options1));
                    }
                }catch (Exception e){
                    //这里有可能没有设置数据集 会有错
                    LogUtils.e(e.getMessage());
                }


            }
        }).isDialog(true).build();
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
                    ConstantUtil.appSelectBean = GsonUtils.fromJson(result, AppSelectBean.class);
                    for (int i = 0; i < ((ConstantUtil.appSelectBean == null || ConstantUtil.appSelectBean.plant_item == null) ? 0 : ConstantUtil.appSelectBean.plant_item.size()); i++) {
                        plant_item_list.add(ConstantUtil.appSelectBean.plant_item.get(i).name);
                    }
//                    JSONArray data = JsonUtils.getJSONArray(result, "plant_item", null);
//                    addToList(data, plant_item_list);
//                    data = JsonUtils.getJSONArray(result, "plant_type", null);
//                    addToList(data, plant_type_list);
//                    data = JsonUtils.getJSONArray(result, "plant_model", null);
//                    addToList(data, plant_model_list);
                }
                initRecycler(root);
            }
        });

    }

    void addToList(JSONArray data, List<String> list) {
        if (data != null && TextUtils.isEmpty(data.toString()) == false) {
            for (int i = 0; i < data.length(); i++) {
                try {
                    list.add(data.getString(i));
                } catch (JSONException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        }
    }

    private void initRecycler(View root) {
        root.findViewById(R.id.labour_fragment3_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addData(0, new Labour3Bean());
                mRecycler.smoothScrollToPosition(0);
            }
        });
        mRecycler = root.findViewById(R.id.laboru3_recyler1);

//        CLPFramgent parentFragment = (CLPFramgent) getParentFragment();
//        if (TextUtils.isEmpty(parentFragment.reportID)){
//            mList.add(new Labour3Bean());
//            mList.add(new Labour3Bean());
//            mList.add(new Labour3Bean());
//        }
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DemoAdapter(mList);
        mRecycler.setAdapter(mAdapter);
    }

    public class DemoAdapter extends BaseQuickAdapter<Labour3Bean, BaseViewHolder> {

        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public DemoAdapter(List<Labour3Bean> list) {
            super(R.layout.layout_recycler_labour3, list);
        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(final BaseViewHolder helper, final Labour3Bean item) {
            helper.setIsRecyclable(false);      //复用会出现问题-->不再复用-->主要这里有EditText添加了addTextChangedListener,复用的时候在设置文本被监听了,会导致问题
            LogUtils.e(helper.getPosition() + item.toString());
            final TextView view_plant_item = helper.getView(R.id.labour3_plant_item);
            setText(view_plant_item, item.title, item.quantity_is_black);
            final TextView type = (TextView) helper.getView(R.id.labour3_plant_type);
            Switch view_switch = helper.getView(R.id.labour3_plant_switch);
            if (item.status_switch == 0) {
                view_switch.setText("W");
                view_switch.setChecked(false);
            } else {
                view_switch.setText("I");
                view_switch.setChecked(true);
            }
            setText(type, item.typeName, item.typeName_is_black);
            setText((TextView) helper.getView(R.id.labour3_quantity), item.quantity + "", item.quantity_is_black);
            setText((TextView) helper.getView(R.id.labour3_owner), item.owner, item.owner_is_black);
            setText((TextView) helper.getView(R.id.labour3_plant_model), item.model, item.model_is_black);
            setText((TextView) helper.getView(R.id.labour3_description), item.itemDesc, item.itemDesc_is_black);
            view_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        item.status_switch = 1;
                        buttonView.setText("I");
                    } else {
                        item.status_switch = 0;
                        buttonView.setText("W");
                    }
                }
            });
            ((EditText) helper.getView(R.id.labour3_quantity)).addTextChangedListener(new MyTextChangedListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (TextUtils.isEmpty(s.toString())) {
                            Integer value = Integer.parseInt(((EditText) helper.getView(R.id.labour3_quantity)).getHint().toString());
                            item.quantity = value;
                        } else {
                            item.quantity_is_black = true;
                            Integer value = Integer.parseInt(s.toString());
                            item.quantity = value;
                        }
                    } catch (Exception e) {
                        item.quantity = 0;
                        LogUtils.e(e.getMessage());
                    }
                }
            });
            ((EditText) helper.getView(R.id.labour3_owner)).addTextChangedListener(new MyTextChangedListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        item.owner = ((EditText) helper.getView(R.id.labour3_owner)).getHint().toString();
                    } else {
                        item.owner_is_black = true;
                        item.owner = s.toString();
                    }
                }
            });
            ((EditText) helper.getView(R.id.labour3_description)).addTextChangedListener(new MyTextChangedListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        item.itemDesc = ((EditText) helper.getView(R.id.labour3_description)).getHint().toString();
                        item.itemDesc_is_black = true;
                    } else {
                        item.itemDesc = s.toString();
                    }
                }
            });
            view_plant_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBean = item;
                    pvOptions.setPicker(plant_item_list);
                    pvOptions.show(v);
                }
            });
            type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(view_plant_item.getText()) && "Plants Items".equals(view_plant_item.getHint())) {
                        ToastUtils.showShort("please select Plants Items");
                        return;
                    }
                    mBean = item;
                    for (int i = 0; i < ((ConstantUtil.appSelectBean == null || ConstantUtil.appSelectBean.plant_item == null) ? 0 : ConstantUtil.appSelectBean.plant_item.size()); i++) {
                        if (ConstantUtil.appSelectBean.plant_item.get(i).name.equals(item.title)){
                            plant_type_list.clear();
                            int id = ConstantUtil.appSelectBean.plant_item.get(i).id;
                            for (int j = 0; j < ConstantUtil.appSelectBean.plant_type.size(); j++) {
                                AppSelectBean.PlantTypeBean typeBean = ConstantUtil.appSelectBean.plant_type.get(j);
                                if (typeBean.pid==id){
                                    plant_type_list.add(typeBean.name);
                                }
                            }
                            break;
                        }
                    }
                    pvOptions.setPicker(plant_type_list);
                    pvOptions.show(v);
                }
            });
            helper.setOnClickListener(R.id.labour3_plant_model, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(type.getText()) && "Items Type".equals(type.getHint())) {
                        ToastUtils.showShort("please select Items Type");
                        return;
                    }
                    mBean = item;
                    for (int i = 0; i < ((ConstantUtil.appSelectBean == null || ConstantUtil.appSelectBean.plant_type == null) ? 0 : ConstantUtil.appSelectBean.plant_type.size()); i++) {
                        if (ConstantUtil.appSelectBean.plant_type.get(i).name.equals(item.typeName)){
                            plant_model_list.clear();
                            int id = ConstantUtil.appSelectBean.plant_type.get(i).id;
                            for (int j = 0; j < ConstantUtil.appSelectBean.plant_model.size(); j++) {
                                AppSelectBean.PlantModelBean typeBean = ConstantUtil.appSelectBean.plant_model.get(j);
                                if (typeBean.pid==id){
                                    plant_model_list.add(typeBean.name);
                                }
                            }
                            break;
                        }
                    }
                    pvOptions.setPicker(plant_model_list);
                    pvOptions.show(v);
                }
            });
            helper.setOnClickListener(R.id.labour3_del, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        //todo容易报异常 --> 由于按的非常快,对同一position进行delete操作
//                        remove(helper.getPosition());      // ArrayIndexOutOfBoundsException: length=10; index=-1
                        mList.remove(item);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                    }
                }
            });
        }

        private void setText(TextView view, String text, boolean is_black) {
            if (!TextUtils.isEmpty(text)) {
                if (is_black) {
                    view.setText(text);
                } else {
                    view.setText("");
                    view.setHint(text);
//                    LogUtils.e(text == null, text);
                }
            } else {
                view.setText("");   //为空也必须设置,不然会复用
            }
        }
    }
}
