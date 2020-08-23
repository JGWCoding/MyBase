package com.example.zw_engineering;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class HomeActivity extends Activity {
    private MediaProjectionManager mMediaProjectionManager;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;
    private ImageReader mImageReader;
    public MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    public static final int REQUEST_MEDIA_PROJECTION = 18;
    ImageView mImageCupView;
    public static HomeActivity mHomeActivity;
    public static String imagePath ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        mHomeActivity = this;
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            startService(new Intent(this, FloatingService.class));
        }

        //   获取屏幕截图 ---> 未完成
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.height = (int) (display.getHeight() * 0.3);
//        params.width = (int) (display.getWidth() * 0.8);
        params.alpha = 1.0f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.flags = params.FLAG_NOT_TOUCH_MODAL
                | params.FLAG_NOT_FOCUSABLE;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);

    //需要一个悬浮窗
        //   发送给指定设备
        //   获取我点的指令
        //   发送指令到对方的设备
        //获取当前屏幕的像素点
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        //5 表示接受的权限的最多次数(拒绝5次就会崩溃)
        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 5);
        //检查其版本号
        requestCapturePermission();
        mImageCupView = (ImageView) findViewById(R.id.cupView);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaProjection == null) {
                    //判断是否获取到权限,否则开启重新开启权限
                    Toast.makeText(HomeActivity.this,"您必须接受开启权限",Toast.LENGTH_SHORT).show();
                    requestCapturePermission();
                }
                startScreenShot();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        ShareUtils.shareWechatFriend(HomeActivity.this,HomeActivity.imagePath);
                    }
                },1000);
            }
        });
    }
        Handler handler1 = new Handler();
    public void startScreenShot() {
        handler1.postDelayed(new Runnable() {
            public void run() {
                //start virtual
                virtualDisplay();
            }
        }, 5);

        handler1.postDelayed(new Runnable() {
            public void run() {
                //capture the screen
                startCapture();
            }
        }, 30);
    }

    private void startCapture() {

        Image image = mImageReader.acquireLatestImage();
            LogUtils.e("iamge is null" ,image==null);
        if (image == null) {
            //开始截屏
            startScreenShot();
        } else {
            //保存截屏
            SaveTask mSaveTask = new SaveTask();
            mSaveTask.execute(image);
//            AsyncTaskCompat.executeParallel(mSaveTask, image);
        }
    }

    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Image... params) {
            if (params == null || params.length < 1 || params[0] == null) {
                return null;
            }
            Image image = params[0];
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_4444);
            LogUtils.e(width + rowPadding / pixelStride, height);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = ImageUtils.compressBySampleSize(bitmap,2,true);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            File fileImage = null;
            if (bitmap != null) {
                try {
//                    fileImage = new File(FileUtil.getScreenShotsName(getApplicationContext()));
                    fileImage = new File(getExternalCacheDir().getAbsolutePath()+System.currentTimeMillis()+".jpg");
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        //发送广播给相册--更新相册图片
//                        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        Uri contentUri = Uri.fromFile(fileImage);
//                        media.setData(contentUri);
//                        sendBroadcast(media);
                    }
                    //发送截图给微信
                    imagePath = fileImage.getAbsolutePath();
//                    ShareUtils.shareWechatFriend(getApplication(),fileImage.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileImage = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileImage = null;
                }
            }

            if (fileImage != null) {
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //预览图片
            if (bitmap != null) {
                bitmap.recycle();
                Toast.makeText(HomeActivity.this,"Activity截图已经成功保存到相册",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this,Home2Activity.class));
                //直接设置动画效果
//                setAnimation(bitmap);
            }
        }
    }
    private void setAnimation(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        ViewGroup.LayoutParams layoutParams = mImageCupView.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        //mImagerCupView是布局文件中创建的
        mImageCupView.setLayoutParams(layoutParams);
        mImageCupView.setImageBitmap(bitmap);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatory = ObjectAnimator.ofFloat(mImageCupView, "scaleY", 1.0f, 0.0f);
        ObjectAnimator animatorx = ObjectAnimator.ofFloat(mImageCupView, "scaleX", 1.0f, 0.0f);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //后面可以继续接着写其他活动，如页面的跳转等等

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.setDuration(500);
        animatorSet.play(animatory).with(animatorx);//两个动画同时开始
        animatorSet.start();
    }

    private void virtualDisplay() {
        if(mMediaProjection != null) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestCapturePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(this, "不能截屏", Toast.LENGTH_LONG).show();
            return;
        }
        //获取截屏的管理器
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(this, FloatingService.class));
            }
        }
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode == RESULT_OK && data != null) {
                    mMediaProjection = mMediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data);
                }
                break;
        }
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }

    // 结束后销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVirtual();
        tearDownMediaProjection();
    }





}
