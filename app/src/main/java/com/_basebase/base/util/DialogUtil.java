package com._basebase.base.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

/**
 * 一般可以写一个layout,弹出自己想要的效果view
 *
 */
public class DialogUtil {
    public static AlertDialog show(Activity activity, View view){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setCancelable(true).create();
        alertDialog.setView(view);
        alertDialog.show();
        return alertDialog;
    }
}
