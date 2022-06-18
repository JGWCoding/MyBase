package com.dome.base.shortcut.util;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;

import com.blankj.utilcode.util.Utils;
import com.dome.base.shortcut.ShortDome1Activity;
import com.example.zw_engineering.R;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class ShortcutUtil {
    //设置动态长按快捷键,清除数据会消失该item,设置过一次后就会展示了
    public static void setupShortcuts() {
        ShortcutManager mShortcutManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            mShortcutManager = getSystemService(Utils.getApp(), ShortcutManager.class);
            List<ShortcutInfo> infos = new ArrayList<>();
            for (int i = 0; i < mShortcutManager.getMaxShortcutCountPerActivity() - mShortcutManager.getManifestShortcuts().size(); i++) {
                Intent intent = new Intent(Utils.getApp(), ShortDome1Activity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("msg", "我和" + i + "的对话");
                ShortcutInfo info = new ShortcutInfo.Builder(Utils.getApp(), "id" + i)
                        .setShortLabel("动态标题short" + i)
                        .setLongLabel("动态长标题short" + i)
                        .setIcon(Icon.createWithResource(Utils.getApp(), R.mipmap.ic_launcher_round))
                        .setIntent(intent)
                        .build();
                infos.add(info);
//            manager.addDynamicShortcuts(Arrays.asList(info));
            }

            mShortcutManager.setDynamicShortcuts(infos);
        }
    }
}
