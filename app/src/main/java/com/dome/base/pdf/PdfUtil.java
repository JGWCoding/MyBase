package com.dome.base.pdf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PdfUtil {

    public void create(){
//        // create a new document
//        PdfDocument document = new PdfDocument();
//
//        // crate a page description
//        PageInfo pageInfo = new PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();
//
//        // start a page
//        Page page = document.startPage(pageInfo);
//
//        // draw something on the page   这个需要在屏幕显示出来的view(不然的话为空)
//        View content = getContentView();
//        content.draw(page.getCanvas());
//
//        // finish the page
//        document.finishPage(page);
//        //...
//        // add more pages
//        // write the document content
//        ....
//        document.writeTo(getOutputStream());
//
//        // close the document
//        document.close();
    }


    public static void createPdf(String report, String location, ArrayList<Uri> uris) {
        PdfDocument document = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
//      // crate a page description
            int width;
            Paint paint = new Paint();
            paint.setStrokeWidth(3);
            paint.setColor(Color.BLACK);
            paint.setTextSize(12 * 3);
            paint.setStyle(Paint.Style.STROKE);
            String text0 = " 顺 源 建 业 有 限 公 司 ";
            String text1 = "Inspection status: " + report;
            Calendar calendar = Calendar.getInstance();
            long millis = calendar.getTimeInMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy   HH:mm:ss");
            String date = simpleDateFormat.format(new Date(millis));
//            String text2 = "Date: 18/03/2020 Time: 13:05";
            String text2 = "Date: "+date;

            String text3 = "Location : " + location;
            FontMetrics fontMetrics = paint.getFontMetrics();
            float textHeight = Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent);


            float text0_width = paint.measureText(text0);
            float text1_width = paint.measureText(text1);
            float text2_width = paint.measureText(text2);

            float text3_width = paint.measureText(text3);
            width = (int) (text0_width + text1_width + text2_width) + 100;

            int font_line = (int) (text3_width / (width==0?1:width )+ 1);

            int fromHeight = 123;
            int height = (int) (fromHeight * 2 + (uris.size() / 2 + uris.size() % 2) * width / 2+3 +(textHeight+20)*font_line);
            width = Math.max(width,1080);
            PageInfo pageInfo = new PageInfo.Builder(width, height, 1).create();
//      // start a page
            Page page = document.startPage(pageInfo);
//
//      // draw something on the page
//        View content = getContentView();
//        content.draw(page.getCanvas());
            Canvas canvas = page.getCanvas();


            Path path = new Path();
            path.moveTo(3, 3);
            path.lineTo(width - 3, 3);
            path.lineTo(width - 3, fromHeight);
            path.lineTo(3, fromHeight);
            path.lineTo(3, 3);

            path.moveTo(text0_width + 20, 3);
            path.lineTo(text0_width + 20, fromHeight);
            path.moveTo(width - text2_width - 20, 3);
            path.lineTo(width - text2_width - 20, fromHeight);

            //第二个格子
            path.moveTo(width - 3, fromHeight);
            path.lineTo(width - 3, fromHeight * 2+(textHeight+20)*font_line);
            path.lineTo(3, fromHeight * 2+(textHeight+20)*font_line);
            path.lineTo(3, fromHeight);




            canvas.drawText(text0, 10, fromHeight / 2 + textHeight / 2, paint);
            canvas.drawText(text1, text0_width + 30, fromHeight / 2 + textHeight / 2, paint);
            canvas.drawText(text2, width - text2_width - 10, fromHeight / 2 + textHeight / 2, paint);

            for (int i = 0; i < font_line+1; i++) {
                if (text3==null||text3.length()<=1) {
                    text3= "   ";
                }
                int index = paint.breakText(text3, true, width, null);
                String temp_text = text3.substring(0, index);
                text3 = text3.substring(index);
                canvas.drawText(temp_text, 10, fromHeight + textHeight*(i+1) + 20, paint);
            }
//            int index = paint.breakText(text3, true, width, null);
//            String tem_text = text3.substring(0, index);
//            text3 = text3.substring(index);
//            canvas.drawText(tem_text, 10, fromHeight + textHeight + 20, paint);
//            canvas.drawText(text3, 10, fromHeight + textHeight * 2 + 20, paint);

            for (int i = 0; i < uris.size(); i++) {
                if (i%2==0){
                    int x = 3;
                    int y = (int) ((textHeight+20)*font_line +fromHeight * 2 + (i / 2 * width / 2)-3);
                    path.moveTo(x,y);
                    path.lineTo(x,y+width/2);
                    path.lineTo(x+width-6,y+width/2);
                    path.lineTo(x+width-6,y);
                    path.moveTo(x+width/2,y);
                    path.lineTo(x+width/2,y+width/2);
                }

                String filePath = FileDirectory.getPath(Utils.getApp(),uris.get(i));
                if (filePath != null && new File(filePath).exists()) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        Bitmap bitmap = getBitmap(file,width/2,width/2);
                        int bitWidth = bitmap.getWidth();
                        int bitHeight = bitmap.getHeight();
                        if (bitWidth <= width / 2 && bitHeight <= width / 2) {
                            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

                            int left = 3 + (i % 2 * width / 2)+(width/2-bitWidth)/2;
                            int top = (int) ((textHeight+20)*font_line+ fromHeight * 2 + 3 + (i / 2 * width / 2) +(width/2-bitHeight)/2);
                            Rect des_rect = new Rect(left, top, left+bitWidth, top+bitHeight);
                            canvas.drawBitmap(bitmap, rect, des_rect, paint);
                        } else {
                            int newWidth;
                            int newHeight;
                            if (bitHeight > bitWidth) {
                                newHeight = width / 2;
                                newWidth = newHeight / bitHeight * bitWidth;
                                Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                                if (newbitmap==null){
                                    newbitmap = bitmap;
                                }
                                Rect rect = new Rect(0, 0, newbitmap.getWidth(), newbitmap.getHeight());
                                int left = 3 + (i % 2 * width / 2)+(width/2-newWidth)/2;
                                int top = (int) ((textHeight+20)*font_line+ fromHeight * 2 + 3 + (i / 2 * width / 2) +(width/2-newHeight)/2);
                                Rect des_rect = new Rect(left, top, left+newWidth, top+newHeight);
                                canvas.drawBitmap(newbitmap, rect, des_rect, paint);
                            } else {
                                newWidth = width / 2;
                                newHeight = newWidth / bitWidth * bitHeight;
                                Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                                if (newbitmap==null){
                                    newbitmap = bitmap;
                                }
                                Rect rect = new Rect(0, 0, newbitmap.getWidth(), newbitmap.getHeight());
                                int left = 3 + (i % 2 * width / 2)+(width/2-newWidth)/2;
                                int top = (int) ((textHeight+20)*font_line+  fromHeight * 2 + 3 + (i / 2 * width / 2) +(width/2-newHeight)/2);
                                Rect des_rect = new Rect(left, top, left+newWidth, top+newHeight);
                                canvas.drawBitmap(newbitmap, rect, des_rect, paint);
                            }
                        }
                        bitmap.recycle();
                    }
                }
            }

            canvas.drawPath(path, paint);
//      // finish the page
            document.finishPage(page);
//// . . .
////      // add more pages
//// . . .
//      // write the document content
            try {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/example.pdf");
                if (file.exists()) {
                    file.delete();
                }
                document.writeTo(new FileOutputStream(file));
            } catch (Exception e) {
                Log.e("mytag", e.getMessage());
            }
//
//      // close the document
            document.close();
        }else {
//            Toast.makeText(this,"手机不支持创建PDF",Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap getBitmap(final File file, final int maxWidth, final int maxHeight) {
        if (file == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }
    public static int calculateInSampleSize(final BitmapFactory.Options options,
                                            final int maxWidth,
                                            final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

}
