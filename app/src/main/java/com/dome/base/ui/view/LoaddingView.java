package com.dome.base.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.zw_engineering.R;

public class LoaddingView {
    private static Dialog dialog;

    private static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        if (TextUtils.isEmpty(msg)) {
            tipTextView.setVisibility(View.GONE);
        } else {
            tipTextView.setText(msg);// 设置加载信息
        }
        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
//        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();

        return loadingDialog;
    }

    public static void show() {
        if (dialog == null) {
            dialog = createLoadingDialog(ActivityUtils.getTopActivity(), "loading...");
        }
        if (dialog != null && dialog.isShowing() == false) {
            dialog.show();
        }
    }

    public static void showLoadding() {
        if (dialog == null) {
            dialog = createLoadingDialog(ActivityUtils.getTopActivity(), null);
        }
        if (dialog != null && dialog.isShowing() == false) {
            dialog.show();
        }
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}


//<style name="MyDialogStyle">
//<item name="android:windowBackground">@android:color/transparent</item>
//<item name="android:windowFrame">@null</item>
//<item name="android:windowNoTitle">true</item>
//<item name="android:windowIsFloating">true</item>
//<item name="android:windowIsTranslucent">true</item>
//<item name="android:windowContentOverlay">@null</item>
//<item name="android:windowAnimationStyle">@android:style/Animation.Dialog</item>
//<item name="android:backgroundDimEnabled">true</item>
//</style>

//   R.layout.dialog_loading
//   dialog_loadding.xml
//    <?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        android:id="@+id/dialog_loading_view"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:gravity="center"
//        android:orientation="vertical">
//
//<LinearLayout
//        android:layout_width="150dp"
//                android:layout_height="110dp"
//                android:background="@drawable/loading_bg"
//                android:gravity="center"
//                android:orientation="vertical"
//                android:paddingBottom="10dp"
//                android:paddingLeft="21dp"
//                android:paddingRight="21dp"
//                android:paddingTop="10dp">
//
//<ProgressBar
//            android:id="@+id/progressBar1"
//                    android:layout_width="35dp"
//                    android:layout_height="35dp"
//                    android:layout_gravity="center_horizontal"
//                    android:indeterminateBehavior="repeat"
//                    android:indeterminateDrawable="@drawable/dialog_loading"
//                    android:indeterminateOnly="true" />
//
//<TextView
//            android:id="@+id/tipTextView"
//                    android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:layout_marginTop="15dp"
//                    android:text="加载中..."
//                    android:textColor="#f0f0f0"
//                    android:textSize="15sp" />
//</LinearLayout>
//
//</LinearLayout>