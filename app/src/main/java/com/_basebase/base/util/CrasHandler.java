package com._basebase.base.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * @Author Liudeli
 * @Describe：控制整个App的异常收集与处理，使用前记得要在Application中初始化initCrasHandler
 */
public class CrasHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    private CrasHandler() {
    }

    private static CrasHandler crasHandler = null;

    public static CrasHandler getInstance() {
        if (null == crasHandler) {
            // synchronized (CrasHandler.class) {
            //if (null == crasHandler) {
            crasHandler = new CrasHandler();
            // }
            // }
        }
        return crasHandler;
    }

    /**
     * 初始化设置（把系统的修改成自身的来控制）
     */
    public void initCrasHandler(Context mContext) {
        this.mContext = mContext;
        //系统默认处理异常Handler
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (isHandler(e)) {
            handlerException(t, e);
        } else {
            uncaughtExceptionHandler.uncaughtException(t, e);
        }
    }

    /**
     * 判断是否为空，才能知道是否需要自己处理
     *
     * @param e
     * @return
     */
    private boolean isHandler(Throwable e) {
        if (null == e) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 处理我需要处理的异常信息，并告知用户，并退出当前应用程序
     *
     * @param t
     * @param ex
     */
    private void handlerException(Thread t, Throwable ex) {
        // 收集异常信息

        try {
            collectException(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果系统提供了默认异常处理就交给系统进行处理，否则自己进行处理。
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(t, ex);
        } else {
            // 结束当前应用程序进程
            android.os.Process.killProcess(android.os.Process.myPid());
            // 结束虚拟机，是否所有内存
            System.exit(0);
        }
    }


    /**
     * 收集异常信息
     *
     * @param e
     */
    private void collectException(final Throwable e) throws IOException, PackageManager.NameNotFoundException {
        //获取系统的日期
        Calendar calendar = Calendar.getInstance();
//年
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH) + 1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
        int second = calendar.get(Calendar.SECOND);

        String time = year + "年" + month + "月" + day + "日" + hour + ":" + minute + ":" + second;
        File directory = Environment.getDataDirectory();
        File file = new File(directory + File.separator + "Crash");
        if (!file.exists()) file.mkdirs();
        writeToSDcard(e, file.getAbsolutePath() + "/crash_" + time + ".txt");

    }

    //将异常写入文件
    private void writeToSDcard(Throwable ex, String PATH) throws IOException, PackageManager.NameNotFoundException {
        //如果没有SD卡，直接返回
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        long currenttime = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currenttime));

        File exfile = new File(PATH);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(exfile)));
        Log.e("错误日志文件路径", "" + exfile.getAbsolutePath());
        pw.println(time);
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        //当前版本号
        pw.println("App Version:" + pi.versionName + "_" + pi.versionCode);
        //当前系统
        pw.println("OS version:" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
        //制造商
        pw.println("Vendor:" + Build.MANUFACTURER);
        //手机型号
        pw.println("Model:" + Build.MODEL);
        //CPU架构
        pw.println("CPU ABI:" + Build.CPU_ABI);

        ex.printStackTrace(pw);

        pw.close();
//        从Throwable 对象中就可以提取出异常信息
//        例如 Throwable e；
//        StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
//        StackTraceElement  这个类就包含了所需要的字段
//
//        private String declaringClass;
//        private String methodName;
//        private String fileName;
//        private int    lineNumber;
//        原文链接：https://blog.csdn.net/xuyw10000/java/article/details/82802438
    }


    //put
//https://api.github.com/repos/JGWCoding/upload/contents/ZW_Engineering/1234.txt?access_token=a81a1930f852a5ef79a31c3f63e3f5dd843b4421  (Content-Type: application/json)
//{"message":"commit1","content":"bXkgbmV3IGZpbGUgY29udGVudHM="}
//上传crash文件到github上
    public static boolean uploadCrashFile(String fullpath) {
        String projectName = AppUtils.getAppName();
        if (TextUtils.isEmpty(projectName)){
            projectName = "ZW_Engineering";
        }
        projectName = projectName.replace(" ","_");
        File file = new File(fullpath);
        if (file.exists()==false||file.length()<=0){
            return false;
        }
//        LogUtils.e(file.getAbsolutePath());
        TimeUtils.date2String(TimeUtils.getNowDate());
        String FileName = TimeUtils.date2String(TimeUtils.getNowDate())+"____"+file.getName();
        FileName = FileName.replace(" ","_");
        String url = "https://api.github.com/repos/JGWCoding/upload/contents/"+projectName+"/"+FileName;
        url += "?access_token=a81a1930f852a5ef79a31c3f63e3f5dd843b4421";
        // https://api.github.com/repos/JGWCoding/upload/contents/bugs123/hello.txt
        byte[] bytes = FileIOUtils.readFile2BytesByStream(file);
        String content = new String(Base64.encode(bytes,Base64.DEFAULT));
//        content = "bXkgbmV3IGZpbGUgY29udGVudHM=";
//put
//https://api.github.com/repos/JGWCoding/upload/contents/ZW_Engineering/1234.txt?access_token=a81a1930f852a5ef79a31c3f63e3f5dd843b4421  (Content-Type: application/json)
//{"message":"commit1","content":"bXkgbmV3IGZpbGUgY29udGVudHM="}
        JSONObject jo = new JSONObject();
        try {
            jo.put("message", Build.MODEL + " commit");
            jo.put("content",content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        RequestBody body = RequestBody.create(json, JSON);
        RequestBody body = RequestBody.create(jo.toString(), MediaType.get("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
//                .addHeader("Authorization","token a81a1930f852a5ef79a31c3f63e3f5dd843b4421")
                .put(body)
                .build();

        try (Response response = new OkHttpClient().newCall(request).execute()) {
            if (response.code()>=200&&response.code()<=299){
                boolean delete = file.delete();
                if (!delete){
                    file.delete();
                }
                return true;
            }
            LogUtils.e("调用了",response.code());
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        LogUtils.e("调用了"+request.toString());
        return false;
    }
}