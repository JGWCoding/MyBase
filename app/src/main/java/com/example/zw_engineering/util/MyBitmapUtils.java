package com.example.zw_engineering.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyBitmapUtils {

    public static void compressImage(Bitmap image, int option) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > (1024 * 1024 * 3)) { // 循环判断如果压缩后图片是否大于3M,大于继续压缩
            if (options == 0) {    //压缩不了3m以下不压缩了
                break;
            }
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(MyPathUtils.getCache(option));
            fileOutputStream.write(baos.toByteArray());
            LogUtils.e(MyPathUtils.getCache(option));
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }


    public static void compressInSampleSize(String path, int option) throws Exception {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        long l = FileUtils.getLength(path) / 1024 / 1024 * 2;
        newOpts.inSampleSize = (int) (Math.sqrt(l) + 0.5f);// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
        compressImage(bitmap,option);
        bitmap.recycle();
    }

    public static Bitmap setBitmapFromDisk(Context context, int res) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置只加载图片的格式尺寸信息到内存，不加载具体的图片字节。
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res, options);
        //获取图片的高度和宽度
        int height = options.outHeight;
        int width = options.outWidth;
        //获取图片的类型
        String imageType = options.outMimeType;
        Log.d("imageTest", height + "    " + width + "   " + imageType);
        System.out.println(height + "    " + width + "   " + imageType);
        //长&&宽压缩的比例,内存占用的比例关系是平方倍
        options.inSampleSize = 4;   //是原来的图片的16分之1
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), res, options);
        return bitmap;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap getBitmap(String path) {
        if(FileUtils.getLength(path)>1024*1024){
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            long l = FileUtils.getLength(path) / 1024 / 1024 ;
            newOpts.inSampleSize = (int) (Math.sqrt(l) + 0.5f);// 设置采样比例
            return BitmapFactory.decodeFile(path,newOpts);
        }else{
            return BitmapFactory.decodeFile(path);
        }
    }
    public static Bitmap getBitmap(byte[] bytes) {
        if(bytes.length>1024*1024){ //大于1M
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            long l = bytes.length / 1024 / 1024 ;
            newOpts.inSampleSize = (int) (Math.sqrt(l) + 0.5f);// 设置采样比例
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length,newOpts);
        }else{
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }
    }
}
