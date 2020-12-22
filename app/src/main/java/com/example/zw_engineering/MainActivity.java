package com.example.zw_engineering;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zw_engineering.util.MyNetWorkUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText name;      //kevin
    private EditText password;  //123456


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyDomeActivity.class);
                intent.putExtra("button","Main");
                startActivity(intent);
            }
        });

//        initView();
//        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA,
//                Manifest.permission.READ_CALL_LOG};
//        if (!checkDangerousPermissions(this, permissions)) {
//            //acitivty中申请权限
//            ActivityCompat.requestPermissions(this, permissions, 1);
//        }
//        PermissionUtils.permission().request();
//        MyPermissionUtils.requestSystemAlertWindow(this);
//        MyPermissionUtils.launchAppDetailsSettings();
//        PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
//            @Override
//            public void onGranted() {
//                LogUtils.e();
//            }
//
//            @Override
//            public void onDenied() {
//                LogUtils.e();
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String dir = Environment.getExternalStorageDirectory() + "/" + "freightRecording/";
//                String old = dir+"Delacey - Dream It Possible.flac";
//
//                for (int i = 0; i < 200; i++) {
//                    new File(dir+i+"_12345678910_SF0890__2_123456_1.flac").delete();
//                }
//                for (int i = 0; i < 200; i++) {
//                    nioTransferCopy(new File(old),new File(dir+i+"_12345678910_SF0890__2_1595236075060_1.flac"));
//                }
//
//            }
//        }).start();

//            startService(new Intent(this,InterceptService.class));
    }
    private static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            close(inStream);
//            close(in);
//            close(outStream);
//            close(out);
        }
    }

    private boolean checkDangerousPermissions(MainActivity ac, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)) {
                return false;
            }
        }
        return true;
    }

    private void initView() {
        name = findViewById(R.id.main_activity_name);
        password = findViewById(R.id.main_activity_password);
        findViewById(R.id.main_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        //login page background
        //ImageView img_bg = findViewById(R.id.main_activity_bg);
//        img_bg.setAlpha(0.5f);
        //img_bg.setBackgroundDrawable(new BitmapDrawable(BitmapUtils.setBitmapFromDisk(this,R.drawable.logo)));
    }

    private void Login() {
        if(BuildConfig.DEBUG){
            startActivity(new Intent(MainActivity.this, Default_bottom.class));
            finish();
            return;
        }
        if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(password.getText())) {
            Toast.makeText(this, "please input id or password", Toast.LENGTH_SHORT).show();
            return;
        }
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return MyNetWorkUtils.login(name.getText().toString(), password.getText().toString());
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.e(result);
                if (TextUtils.isEmpty(result)) {
                    ToastUtils.showLong("login fail");
                } else {
                    int code = JsonUtils.getInt(result, "code");
                    if (code==200){
                        startActivity(new Intent(MainActivity.this, Default_bottom.class));
                        finish();
                    }else{
                        ToastUtils.showLong("login fail");
                    }
                }
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                ToastUtils.showLong("login fail");
            }
        });

        // Toast.makeText(this,"please input id or password",Toast.LENGTH_SHORT).show();

    }
}
