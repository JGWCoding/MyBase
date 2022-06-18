package com.dome.base.email;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.util.ArrayList;

import androidx.core.content.FileProvider;

public class EmailUtil {
    public static void send(){  //会有许多选择
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] tos = { "way.ping.li@gmail.com" };
        String[] ccs = { "way.ping.li@gmail.com" };
        String[] bccs = {"way.ping.li@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, tos);   //可以多个接收者
        intent.putExtra(Intent.EXTRA_CC, ccs);
        intent.putExtra(Intent.EXTRA_BCC, bccs);
//        ArrayList<String> list = new ArrayList<>();
//        list.add("body");
        intent.putExtra(Intent.EXTRA_TEXT, "body");   //todo 这里需要ArrayList 华为手机是这样
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/example.pdf");
        Uri fileuri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //大于等于版本24（7.0）的场合  ---  "my.fileprovider"需要自身配置
            fileuri = FileProvider.getUriForFile(Utils.getApp(), "my.fileprovider", file);
        } else {
            //小于android 版本7.0（24）的场合
            fileuri = Uri.fromFile(file);
        }
        intent.putExtra(Intent.EXTRA_STREAM, fileuri);
//        intent.setType("*/*");
//        intent.setType("image/*");
//        intent.setType("message/rfc882");
        Intent choose_email_client = Intent.createChooser(intent, "Choose Email Client");
        Utils.getApp().startActivity(choose_email_client);
    }
    public static void sendTo(){    //指定了邮箱发送  mailto:   后面为收件邮箱地址
        Intent data=new Intent(Intent.ACTION_SENDTO);
        String[] tos = { "way.ping.li@gmail.com" };
        data.putExtra(Intent.EXTRA_EMAIL, tos);   //可以多个接收者
        data.setData(Uri.parse("mailto:way.ping.li@gmail.com"));//收件人邮箱
        data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
        data.putExtra(Intent.EXTRA_TEXT, "这是内容");
        Utils.getApp().startActivity(Intent.createChooser(data,"Sending email..."));
//        Utils.getApp().startActivity(data);
    }

    public static void sendMULTIPLE(){    //指定了邮箱发送  mailto:   后面为收件邮箱地址
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        String[] tos = { "way.ping.li@gmail.com"}; //可以多个接收者
        intent.putExtra(Intent.EXTRA_EMAIL, tos);
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
        intent.putExtra(Intent.EXTRA_TEXT, "这是内容");

        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/example.pdf");
        Uri fileuri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //大于等于版本24（7.0）的场合  ---  "my.fileprovider"需要自身配置
            fileuri = FileProvider.getUriForFile(Utils.getApp(), "my.fileprovider", file);
        } else {
            //小于android 版本7.0（24）的场合
            fileuri = Uri.fromFile(file);
        }
        imageUris.add(fileuri);
        imageUris.add(fileuri);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//        intent.setType("image/*");
//        intent.setType("message/rfc882");
        Utils.getApp().startActivity(Intent.createChooser(intent,"Sending email..."));
    }

    public static void sendToActivity(){    //指定了邮箱发送  mailto:   后面为收件邮箱地址

         final String[] NARMAL_PHONE = {"com.android.email", "com.android.email.activity.MessageCompose"};
         final String[] MIUI_PHONE = {"com.android.email", "com.kingsoft.mail.compose.ComposeActivity"};
         final String[] SAMSUNG_PHONE = {"com.samsung.android.email.provider", "com.samsung.android.email.composer.activity.MessageCompose"};

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/example.pdf");
        Uri fileuri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //大于等于版本24（7.0）的场合  ---  "my.fileprovider"需要自身配置
            fileuri = FileProvider.getUriForFile(Utils.getApp(), "my.fileprovider", file);
        } else {
            //小于android 版本7.0（24）的场合
            fileuri = Uri.fromFile(file);
        }
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, fileuri);
            if (getDeviceBrand().toUpperCase().contains("HONOR") || getDeviceBrand().toUpperCase().contains("HUAWEI") || getDeviceBrand().toUpperCase().contains("NUBIA")) {
                intent.setClassName(NARMAL_PHONE[0], NARMAL_PHONE[1]);
            } else if (getDeviceBrand().toUpperCase().contains("XIAOMI") || getDeviceBrand().toUpperCase().contains("XIAOMI")) {
                intent.setClassName(MIUI_PHONE[0], MIUI_PHONE[1]);
            } else if (getDeviceBrand().toUpperCase().contains("SAMSUNG")) {
                intent.setClassName(SAMSUNG_PHONE[0], SAMSUNG_PHONE[1]);
            }
            Utils.getApp(). startActivity(Intent.createChooser(intent, "分享一下"));
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, "");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, fileuri);
            Utils.getApp().startActivity(Intent.createChooser(intent, "分享一下"));
        }
    }
    public static String getDeviceBrand() {
        Log.e("--获取手机厂商--:", Build.BRAND);
        return Build.BRAND;
    }
}
