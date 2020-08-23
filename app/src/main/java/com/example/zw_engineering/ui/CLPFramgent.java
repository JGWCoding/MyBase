package com.example.zw_engineering.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zw_engineering.R;
import com.example.zw_engineering.bean.Labour3Bean;
import com.example.zw_engineering.bean.LabourBean;
import com.example.zw_engineering.ui.clp_fragment.LabourFragment;
import com.example.zw_engineering.ui.clp_fragment.LabourFragment3;
import com.example.zw_engineering.ui.view.LoaddingView;
import com.example.zw_engineering.util.ConstantUtil;
import com.example.zw_engineering.util.MyBitmapUtils;
import com.example.zw_engineering.util.MyFragmentUtils;
import com.example.zw_engineering.util.MyNetWorkUtils;
import com.example.zw_engineering.util.MyPathUtils;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CLPFramgent extends Fragment {
    ArrayList<String> tabIndicators;
    ArrayList<Fragment> tabFragments;
    private LabourFragment mNightLabour;
    private LabourFragment mDayLabour;
    private LabourFragment3 mLabour3;
    String reportType = "";
    public String reportID = "";
    int id;
    OptionsPickerView pvOptions;
    String path;
    ArrayList<String> path_list = new ArrayList<>();

    // 这里按了save,会一直添加,改为更新接口就可以了
    public static Fragment getInstance(String reportType, int id, String reportID) {
        CLPFramgent framgent = new CLPFramgent();
        Bundle bundle = new Bundle();
        bundle.putString("reportType", reportType);
        bundle.putInt("id", id);
        bundle.putString("reportID", reportID);
        framgent.setArguments(bundle);
        return framgent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_clp, container, false);
        root.findViewById(R.id.clp_fragment_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        Bundle bundle = getArguments();
        reportType = bundle.getString("reportType");
        id = bundle.getInt("id");
        reportID = bundle.getString("reportID");
        path_list.add(null);    //初始化
        path_list.add(null);
        if (id == -1) {
            ToastUtils.showLong("id is error,please return");
        }

        initTab(root);
        initView(root);
        LogUtils.e("id = " + id + "  reportID= " + reportID);
        if (!TextUtils.isEmpty(reportID)) {
            root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setInfo();
                }
            });

        }
        ConstantUtil.pic_url1 = "";
        ConstantUtil.pic_url2 = "";
        ConstantUtil.path_list[0] = "";
        ConstantUtil.path_list[1] = "";//对以前选的path清空
        FileUtils.deleteAllInDir(MyPathUtils.getCache());   //删除缓存图片文件
        return root;
    }

    private void setInfo() {
        LoaddingView.show();
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.getDetails(reportID, reportType);
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                ToastUtils.showLong("net work error");
                LoaddingView.dismiss();
            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (!TextUtils.isEmpty(result)) {
                        JSONObject jo = new JSONObject(result);
                        if (jo.getInt("code") == 200) {
                            JSONArray day_shift = jo.getJSONArray("day_shift");
                            mDayLabour.item_title_list.clear();
                            mDayLabour.item_childs_list.clear();
                            if (TextUtils.isEmpty(day_shift.toString()) == false && day_shift.length() > 0) {
                                for (int i = 0; i < day_shift.length(); i++) {
                                    String item = day_shift.getJSONObject(i).getString("item");
                                    ArrayList<LabourBean> beans;
                                    if (mDayLabour.item_title_list.contains(item)) {
                                        beans = mDayLabour.item_childs_list.get(mDayLabour.item_title_list.indexOf(item));
                                    } else {
                                        mDayLabour.item_title_list.add(item);
                                        beans = new ArrayList<>();
                                        mDayLabour.item_childs_list.add(beans);
                                    }
                                    addBean(day_shift, i, beans);
                                }
                            }
                            mDayLabour.mRecycler.getAdapter().notifyDataSetChanged();

                            JSONArray night_shift = jo.getJSONArray("night_shift");
                            mNightLabour.item_title_list.clear();
                            mNightLabour.item_childs_list.clear();
                            if (TextUtils.isEmpty(night_shift.toString()) == false && night_shift.length() > 0) {
                                for (int i = 0; i < night_shift.length(); i++) {
                                    String item = night_shift.getJSONObject(i).getString("item");
                                    ArrayList<LabourBean> beans;
                                    if (mNightLabour.item_title_list.contains(item)) {
                                        beans = mNightLabour.item_childs_list.get(mNightLabour.item_title_list.indexOf(item));
                                    } else {
                                        mNightLabour.item_title_list.add(item);
                                        beans = new ArrayList<>();
                                        mNightLabour.item_childs_list.add(beans);
                                    }
                                    addBean(night_shift, i, beans);
                                }
                            }
                            mNightLabour.mRecycler.getAdapter().notifyDataSetChanged();
                            JSONArray data_plant = jo.getJSONArray("data_plant");
                            mLabour3.mList.clear();
                            if (TextUtils.isEmpty(data_plant.toString()) == false && data_plant.length() > 0) {
                                for (int i = 0; i < data_plant.length(); i++) {
                                    String item = data_plant.getJSONObject(i).getString("item");
                                    String typeName = data_plant.getJSONObject(i).getString("typeName");
                                    int quantity = data_plant.getJSONObject(i).getInt("quantity");
                                    String model = data_plant.getJSONObject(i).getString("model");
                                    String owner = data_plant.getJSONObject(i).getString("owner");
                                    String itemDesc = data_plant.getJSONObject(i).getString("itemDesc");
                                    int item_status = data_plant.getJSONObject(i).getInt("item_status");
                                    Labour3Bean labourBean = new Labour3Bean(item, typeName, model, owner, quantity, itemDesc,item_status);
                                    mLabour3.mList.add(labourBean);
                                }
                            }
                            if (mLabour3.mRecycler != null && mLabour3.mRecycler.getAdapter() != null) {
                                mLabour3.mRecycler.getAdapter().notifyDataSetChanged();
                            }
                            ConstantUtil.pic_url1 = jo.getString("pic1");
                            ConstantUtil.pic_url2 = jo.getString("pic2");
                        } else {
                            ToastUtils.showShort(result);
                        }
                    } else {
                        ToastUtils.showShort("data is empty");
                    }
                } catch (JSONException e) {
                    LogUtils.e(e.getMessage());
                    ToastUtils.showShort("data parsing failed");
                }
                LogUtils.e(result);
                LoaddingView.dismiss();
            }
        });
    }

    private void addBean(JSONArray day_shift, int i, ArrayList<LabourBean> beans) throws JSONException {
        String typeName = day_shift.getJSONObject(i).getString("typeName");
        int quantity = day_shift.getJSONObject(i).getInt("quantity");
        String owner = day_shift.getJSONObject(i).getString("owner");
        String item = day_shift.getJSONObject(i).getString("item");
        LabourBean labourBean = new LabourBean(typeName, quantity + "", owner, false, false, false,item);
//        LogUtils.e(labourBean.quantity+labourBean.title);
        beans.add(labourBean);
    }

    int option; //当前选择的是第几张图片

    private void initView(View root) {
        TextView view_title = root.findViewById(R.id.clp_fragment_title);
        view_title.setText(reportType);
        pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                option = options1;
                if (option2 == 0) {
                    pickFile(0);
                } else {
                    pickFile(1);
                }
//                pickFile(options1);

            }
        }).isDialog(true).build();
        ArrayList<String> list = new ArrayList<>();
//        list.add("take photo");
//        list.add("select photo");
//        list.add("select pdf");
//        pvOptions.setPicker(list);
        list.add("picture_1");
        list.add("picture_2");
        ArrayList<ArrayList<String>> item_list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ArrayList<String> items = new ArrayList<>();
            items.add("take photo");
            items.add("select photo");
            item_list.add(items);
        }
        pvOptions.setPicker(list, item_list);
        root.findViewById(R.id.clp_fragment_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo ?需不需要传递数据
                MyFragmentUtils.addNotMain(getActivity().getSupportFragmentManager(), new AddPhotoFragment(), R.id.nav_host_fragment, false, true);
//                pvOptions.show();
            }
        });

        root.findViewById(R.id.clp_fragment_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploadJson = "";
                uploadJson = getJsonData(uploadJson);
                upload(uploadJson);
            }
        });
    }

    private String getJsonData(String uploadJson) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("reportType", reportType);   //从 Addition 页面来
            jo.put("srid", id); //从 Addition 页面来
//                    putFile(jo);
            JSONArray ja = new JSONArray();
            jo.put("labour", ja);
            for (int i = 0; i < mNightLabour.item_title_list.size(); i++) {
                ArrayList<LabourBean> beans = mNightLabour.item_childs_list.get(i);
                for (int j = 0; j < beans.size(); j++) {
                    JSONObject jobject = new JSONObject();
                    jobject.put("item", mNightLabour.item_title_list.get(i));
                    jobject.put("typeName", getText(beans.get(j).title, "Labour Type"));
                    jobject.put("quantity", beans.get(j).quantity);
                    jobject.put("owner", getText(beans.get(j).owner, "owner"));
                    jobject.put("day_or_night", 2);
                    ja.put(jobject);
                }
            }
            for (int i = 0; i < mDayLabour.item_title_list.size(); i++) {
                ArrayList<LabourBean> beans = mDayLabour.item_childs_list.get(i);
                for (int j = 0; j < beans.size(); j++) {
                    JSONObject jobject = new JSONObject();
                    jobject.put("item", mDayLabour.item_title_list.get(i));
                    jobject.put("typeName", getText(beans.get(j).title, "Labour Type"));
                    jobject.put("quantity", beans.get(j).quantity);
                    jobject.put("owner", getText(beans.get(j).owner, "owner"));
                    jobject.put("day_or_night", 1);
                    ja.put(jobject);
                }
            }
            ja = new JSONArray();
            jo.put("plant", ja);
            for (int i = 0; i < mLabour3.mList.size(); i++) {
                Labour3Bean bean = mLabour3.mList.get(i);
                JSONObject jobject = new JSONObject();
                jobject.put("item", getText(bean.title, ""));
                jobject.put("typeName", getText(bean.typeName, ""));
                jobject.put("quantity", bean.quantity);
                jobject.put("owner", getText(bean.owner, ""));
                jobject.put("model", getText(bean.model, ""));
                jobject.put("itemDesc", getText(bean.itemDesc, ""));
                jobject.put("item_status", bean.status_switch);
                ja.put(jobject);
            }
            uploadJson = jo.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return uploadJson;
    }

    public String getText(String src, String defalut) {
        if (TextUtils.isEmpty(src)) {
            return defalut;
        } else {
            return src;
        }
    }


    // 打开系统的文件选择器
    public void pickFile(int position) {
        switch (position) {
            case 0:
//                PictureSelector.create(getActivity())
//                        .openCamera(PictureMimeType.ofImage())
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
                File outputImage = new File(getContext().getExternalCacheDir(), "output_image.jpg");
                path = outputImage.getAbsolutePath();
               /* 从Android 6.0系统开始，读写SD卡被列为了危险权限，如果将图片存放在SD卡的任何其他目录，
                  都要进行运行时权限处理才行，而使用应用关联 目录则可以跳过这一步
                */
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*
                   7.0系统开始，直接使用本地真实路径的Uri被认为是不安全的，会抛 出一个FileUriExposedException异常。
                   而FileProvider则是一种特殊的内容提供器，它使用了和内 容提供器类似的机制来对数据进行保护，
                   可以选择性地将封装过的Uri共享给外部，从而提高了 应用的安全性
                 */
                Uri imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //大于等于版本24（7.0）的场合
                    imageUri = FileProvider.getUriForFile(getContext(), "my.fileprovider", outputImage);
                } else {
                    //小于android 版本7.0（24）的场合
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent2, 0);
                break;
            case 1:
//                PictureSelector.create(getActivity())
//                        .openGallery(PictureMimeType.ofImage())
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("image/*");
                this.startActivityForResult(intent1, 0);
                break;
            case 2:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                this.startActivityForResult(intent, 0);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                //                case PictureConfig.CHOOSE_REQUEST:
//                    // 图片、视频、音频选择结果回调
//                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                    path = selectList.get(0).getPath();
//                    // 例如 LocalMedia 里面返回三种path
//                    // 1.media.getPath(); 为原图path
//                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
//                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
//                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    break;
                case 0:
                    if (TextUtils.isEmpty(path)) {
                        File outputImage = new File(MyPathUtils.getCache(), "output_image.jpg");
                        path = outputImage.getAbsolutePath();
                    }
                    break;
                case 1:
                    if (data != null && data.getData() != null) {
                        Uri uri = data.getData();
                        path = new MyPathUtils().getPath(uri, getActivity());
                        LogUtils.e(path);
                    } else {
                        path = null;
                    }
                    break;
            }
            if (FileUtils.isFileExists(path)) {
                path_list.add(option, path);
                compressImage(option, path);

//                byte[] bytes = FileIOUtils.readFile2BytesByChannel(path);
//                String img = Base64.encodeToString(bytes, Base64.DEFAULT);
                ToastUtils.showShort("file selelt success");
            } else {
//                ToastUtils.setGravity(Gravity.CENTER,0,0);
                ToastUtils.showLong("Files is not exist");
//                DialogUtil.alertDialog(this, "File is too large", "Files over 2MB", true, null);
            }
        }
        LogUtils.e(path);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (AddPhotoFragment.bitmap1!=null){
            AddPhotoFragment.bitmap1.recycle();
            AddPhotoFragment.bitmap1=null;
        }
        if (AddPhotoFragment.bitmap2!=null){
            AddPhotoFragment.bitmap2.recycle();
            AddPhotoFragment.bitmap2=null;
        }
    }

    // 在这进行图片压缩至3M
    private void compressImage(final int option, final String path) {
        if (option == 1) {
            ConstantUtil.zip_img_state_1 = 0;
        } else {
            ConstantUtil.zip_img_state_2 = 0;
        }
        if (FileUtils.getLength(path) < 3 * 1024 * 1024) {
            if (option == 1) {
                ConstantUtil.zip_img_state_1 = 3;
            } else {
                ConstantUtil.zip_img_state_2 = 3;
            }
            return;//图片小于3M不做处理
        }
//        BitmapUtils
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                MyBitmapUtils.compressInSampleSize(path, option);
                return "";
            }

            @Override
            public void onFail(Throwable t) {
                if (option == 1) {
                    ConstantUtil.zip_img_state_1 = 2;
                } else {
                    ConstantUtil.zip_img_state_2 = 2;
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                if (option == 1) {
                    ConstantUtil.zip_img_state_1 = 1;
                } else {
                    ConstantUtil.zip_img_state_2 = 1;
                }
            }
        });
    }


    private void upload(final String uploadJson) {
        LogUtils.e(uploadJson);
        LoaddingView.show();
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
//                if (TextUtils.isEmpty(reportID)) {
//                    return MyNetWorkUtils.addDetails(uploadJson);
//                } else {
//                    return MyNetWorkUtils.updateDetails(uploadJson);
//                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("json", uploadJson);
                LogUtils.e("上传文件:" + "压缩1文件状态:" + ConstantUtil.zip_img_state_1 + "压缩2文件状态:" + ConstantUtil.zip_img_state_2);
                for (int i = 0; i < 2; i++) {
                    path = ConstantUtil.path_list[i];
//                    LogUtils.e(path == null, path);
                    if (FileUtils.isFile(path) && FileUtils.getLength(path) > 0) {
                        if (i == 0) {
                            while (ConstantUtil.zip_img_state_1 == 0) {
                                LogUtils.e("压缩中");
                                Thread.sleep(100);
                            }
                            if (ConstantUtil.zip_img_state_1 == 3) {
                                map.put("pic1", new File(path));
                            } else if (ConstantUtil.zip_img_state_1 == 1) {
                                if (new File(MyPathUtils.getCache(0)).exists()) {
                                    map.put("pic1", MyPathUtils.getCache(0));
                                }else{
                                    ToastUtils.showLong("upload picture is not exist");
                                }
                            } else if (ConstantUtil.zip_img_state_1 == 2) {
                                ToastUtils.showLong("File compression failed,please select picture 1");
                                return "";
                            }
                        }
                        if (i == 1) {
                            while (ConstantUtil.zip_img_state_2 == 0) {
                                Thread.sleep(100);
                            }
                            if (ConstantUtil.zip_img_state_2 == 3) {
                                map.put("pic2", new File(path));
                            } else if (ConstantUtil.zip_img_state_2 == 1) {
                                if (new File(MyPathUtils.getCache(1)).exists()) {
                                    map.put("pic2", MyPathUtils.getCache(1));
                                }else{
                                    ToastUtils.showLong("upload picture is not exist");
                                }
                            } else if (ConstantUtil.zip_img_state_2 == 2) {
                                ToastUtils.showLong("File compression failed,please select picture 1");
                                return "";
                            }
                        }
                    }
                }
                return MyNetWorkUtils.updateDetails(map);
            }

            @Override
            public void onFail(Throwable t) {
                ToastUtils.showLong("add fail");
                LoaddingView.dismiss();
            }

            @Override
            public void onSuccess(String result) {
                if (JsonUtils.getInt(result, "code") == 200) {
                    ToastUtils.showLong("add success");
                    getActivity().onBackPressed();
                } else {
                    ToastUtils.showLong("add fail");
                }
                LogUtils.e(result);
                LoaddingView.dismiss();
            }
        });
    }

    private void initTab(View root) {
        TabLayout tabLayout = root.findViewById(R.id.clp_fragment_tabs);
        ViewPager viewPager = root.findViewById(R.id.clp_fragment_vp);
        tabIndicators = new ArrayList<>();
        tabIndicators.add("Labour\n(Day Shift)");
        tabIndicators.add("Labour\n(Night Shift)");
        tabIndicators.add("Plant");
        tabFragments = new ArrayList<>();
        mNightLabour = new LabourFragment();
        mDayLabour = new LabourFragment();
        mLabour3 = new LabourFragment3();
        tabFragments.add(mDayLabour);
        tabFragments.add(mNightLabour);
        tabFragments.add(mLabour3);
        ContentPagerAdapter contentAdapter = new ContentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(contentAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.setTabMode(TabLayout.INDICATOR_GRAVITY_CENTER);
        tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.gray), ContextCompat.getColor(getActivity(), R.color.red));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.red));
        ViewCompat.setElevation(tabLayout, 10);
        tabLayout.setupWithViewPager(viewPager);

    }


    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //不会销毁任何事物
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

}
