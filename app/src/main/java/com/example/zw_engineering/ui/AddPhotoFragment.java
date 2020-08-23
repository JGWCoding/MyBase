package com.example.zw_engineering.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zw_engineering.R;
import com.example.zw_engineering.base.BaseFragment;
import com.example.zw_engineering.base.BaseHolder;
import com.example.zw_engineering.base.LoadingPager;
import com.example.zw_engineering.util.ConstantUtil;
import com.example.zw_engineering.util.MyBitmapUtils;
import com.example.zw_engineering.util.MyNetWorkUtils;
import com.example.zw_engineering.util.MyPathUtils;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class AddPhotoFragment extends BaseFragment {

    OptionsPickerView pvOptions;
    int select_index;
    public  static Bitmap bitmap1;  //进行一个小的内存缓存 ---在CLPFragment中销毁
    public  static Bitmap bitmap2;  //进行一个小的内存缓存
    private AddPhotoHolder photoHolder;
    String path;

    @Override
    public LoadingPager.LoadedResult initData() {
        if (bitmap1 == null) {
            if (TextUtils.isEmpty(ConstantUtil.path_list[0])) {
                if (!TextUtils.isEmpty(ConstantUtil.pic_url1)) {
                    byte[] picture_1 = MyNetWorkUtils.getPicture(ConstantUtil.pic_url1);
                    bitmap1 = MyBitmapUtils.getBitmap(picture_1);
                }
            } else {
                bitmap1 = MyBitmapUtils.getBitmap(ConstantUtil.path_list[0]);
            }
        }
        if (bitmap2 == null) {
            if (TextUtils.isEmpty(ConstantUtil.path_list[1])) {
                if (!TextUtils.isEmpty(ConstantUtil.pic_url2)) {
                    byte[] picture_2 = MyNetWorkUtils.getPicture(ConstantUtil.pic_url2);
                    bitmap2 = MyBitmapUtils.getBitmap(picture_2);
                }
            } else {
                bitmap2 = MyBitmapUtils.getBitmap(ConstantUtil.path_list[1]);
            }
        }
        return LoadingPager.LoadedResult.SUCCESS;
    }

    @Override
    public View initSuccessView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {   //有时候会报错
        pvOptions = new OptionsPickerBuilder(inflater.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (options1 == 0) {
                    pickFile(0);
                } else {
                    pickFile(1);
                }
//                pickFile(options1);
            }
        }).isDialog(true).build();
        ArrayList<String> list = new ArrayList<>();
        list.add("take photo");
        list.add("select photo");
        pvOptions.setPicker(list);
        photoHolder = new AddPhotoHolder();
        photoHolder.setDataAndRefreshHolderView("");
        return photoHolder.mHolderView;
    }

    // 打开系统的文件选择器
    public void pickFile(int position) {
        switch (position) {
            case 0:
                File outputImage = new File(MyPathUtils.getCache(), "output_image.jpg");
                path = outputImage.getAbsolutePath();
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("image/*");
                this.startActivityForResult(intent1, 1);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
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
                ConstantUtil.path_list[select_index] = path;
                compressImage(select_index, path);
                if (select_index == 0) {
                    bitmap1 = MyBitmapUtils.getBitmap(path);
                    ViewGroup.LayoutParams params = photoHolder.img1.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    photoHolder.img1.setLayoutParams(params);
                    photoHolder.img1.setImageBitmap(bitmap1);
                } else {
                    bitmap2 = MyBitmapUtils.getBitmap(path);
                    ViewGroup.LayoutParams params = photoHolder.img2.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    photoHolder.img2.setLayoutParams(params);
                    photoHolder.img2.setImageBitmap(bitmap2);
                }
//                ToastUtils.showShort("file selelt success");
            } else {
                ToastUtils.showLong("Files is not exist");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 在这进行图片压缩至3M
    private void compressImage(final int option, final String path) {
        if (option == 0) {
            ConstantUtil.zip_img_state_1 = 0;
        } else {
            ConstantUtil.zip_img_state_2 = 0;
        }
        if (FileUtils.getLength(path) < 3 * 1024 * 1024) {
            if (option == 0) {
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
                if (option == 0) {
                    ConstantUtil.zip_img_state_1 = 2;
                } else {
                    ConstantUtil.zip_img_state_2 = 2;
                }
                LogUtils.e(t.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                if (option == 0) {
                    ConstantUtil.zip_img_state_1 = 1;
                } else {
                    ConstantUtil.zip_img_state_2 = 1;
                }
            }
        });
    }

    class AddPhotoHolder extends BaseHolder<String> {

        ImageView img1;
        ImageView img2;

        @Override
        public View initHolderView() {
            return View.inflate(ActivityUtils.getTopActivity(), R.layout.fragment_add_photo, null);
        }

        @Override
        protected void initView(View root) {
            View l1 = root.findViewById(R.id.photo_fragment_l1);
            l1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_index = 0;
                    pvOptions.show();
                }
            });
            View l2 = root.findViewById(R.id.photo_fragment_l2);
            l2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_index = 1;
                    pvOptions.show();
                }
            });
            img1 = root.findViewById(R.id.photo_fragment_img1);
            img2 = root.findViewById(R.id.photo_fragment_img2);
            root.findViewById(R.id.photo_fragment_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }

        @Override
        public void refreshHolderView(String data) {
            if (bitmap1 != null) {
                //设置图片宽高
                ViewGroup.LayoutParams params = img1.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                img1.setLayoutParams(params);
                //通过imageview，设置图片
                img1.setImageBitmap(bitmap1);
            }
            if (bitmap2 != null) {
                ViewGroup.LayoutParams params = img2.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                img2.setLayoutParams(params);
                //通过imageview，设置图片
                img2.setImageBitmap(bitmap2);
            }
        }
    }
}
