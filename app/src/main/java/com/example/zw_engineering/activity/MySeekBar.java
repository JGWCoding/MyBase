package com.example.zw_engineering.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

import com.example.zw_engineering.R;

@SuppressLint("AppCompatCustomView")
public class MySeekBar extends SeekBar {
    SeekBar seekbar;
    public MySeekBar(Context context) {
        this(context,null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        seekbar = this;
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeProColor(hasFocus);
            }
        });
    }


    private void changeProColor(boolean hasFocus)
    {
        Rect thumb = seekbar.getThumb().getBounds();

        if (hasFocus)
        {
//            Debugger.d("proDrawable "+layerDrawable.getIntrinsicHeight());
            seekbar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_setting_focus));
//            proDrawable.setColorFilter(getResources().getColor(R.color.c_5b), PorterDuff.Mode.SRC);
            Drawable drawable = getResources().getDrawable(R.drawable.seekbar_thumb_unfocus);
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
                //竖屏下获取到图片跟横屏大小一致 需转小
                Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
                Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, config);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
                drawable.draw(canvas);
                drawable = new BitmapDrawable(bitmap);
//                Debugger.d("proDrawable "+drawable.getIntrinsicWidth()+"__"+drawable.getIntrinsicHeight());
            }
            seekbar.setThumb(drawable);
        } else
        {
            seekbar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_setting));
//            proDrawable.setColorFilter(getResources().getColor(R.color.c_4b), PorterDuff.Mode.SRC);
            Drawable drawable = getResources().getDrawable(R.drawable.thumb_setting);
            drawable.setBounds(thumb);
            seekbar.setThumb(drawable);
        }
    }
}
