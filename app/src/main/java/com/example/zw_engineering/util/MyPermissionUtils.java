package com.example.zw_engineering.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public final class MyPermissionUtils {
        public static void requestSystemAlertWindow(Activity activity){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                LogUtils.e(Settings.canDrawOverlays(Utils.getApp()));
                if (!Settings.canDrawOverlays(Utils.getApp())){
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + Utils.getApp().getPackageName()));
                    LogUtils.e(!isIntentAvailable(intent));
                    if (!isIntentAvailable(intent)) {
                        launchAppDetailsSettings();
                        return;
                    }
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivityForResult(intent, 1);
                }
            }
        }

    /**
     * Launch the application's details settings.
     */
    public static void launchAppDetailsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + Utils.getApp().getPackageName()));
        if (!isIntentAvailable(intent)) return;
        Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private static boolean isIntentAvailable(final Intent intent) {
        return Utils.getApp()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }
    public void request(Activity ac, String[] permissions){
        if (!checkDangerousPermissions(ac, permissions)) {
            ActivityCompat.requestPermissions(ac, permissions, 1);
        }
    }
    private boolean checkDangerousPermissions(Activity ac, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(ac, permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)) {
                return false;
            }
        }
        return true;
    }
}