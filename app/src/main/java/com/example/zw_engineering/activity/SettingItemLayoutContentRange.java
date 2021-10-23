//package com.example.zw_engineering.activity;
//
//import android.content.Context;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.PixelFormat;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.LayerDrawable;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.SystemClock;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.skyworth.ui.define.SkyTextSize;
//import com.tianci.setting.Debugger;
//import com.tianci.setting.R;
//import com.tianci.setting.conf.SettingConfig;
//import com.tianci.system.data.TCRangeSetData;
//
///**
// * @Date : 2015年2月10日
// * @Author : Zhan Yu
// * @Description : TODO
// */
//public class SettingItemLayoutContentRange extends SettingItemLayoutContentBaseFocus
//{
//
//    // RangeSeekbar seekbar;
//    SeekBar seekbar;
//    TextView valueTv;
//
//    LayerDrawable layerDrawable;
//    Drawable proDrawable;
//    private boolean haseFocuse=false;
//
//    int max;
//    int min = 0;
//    int current;
//    private int offset = 0; // 对于有负数的情况.
//    private long lastDataChangeTime;
//    private Handler handler = new Handler(Looper.getMainLooper());
//    private Runnable delayRunnable = new Runnable() {
//        @Override
//        public void run() {
//            set(true);
//        }
//    };
//    // TODO 注意回收 Drawable
//    public SettingItemLayoutContentRange(Context context,boolean isShort)
//    {
//        super(context,isShort);
//
//        // seekbar = new RangeSeekbar(context);
//        LayoutInflater layoutInflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        seekbar = (SeekBar) layoutInflater.inflate(R.layout.setting_item_seekbar, null);
//        LayoutParams progressbarParams = new LayoutParams(SettingConfig.getResolutionValue(366-28),
//                SettingConfig.getResolutionValue(80));
//        progressbarParams.rightMargin = SettingConfig.getResolutionValue(28);
//        seekbar.setLayoutParams(progressbarParams);
//        initSeekbar();
//        realContentLayout.addView(seekbar);
//
//        valueTv = new TextView(context);
//        LayoutParams valueTvParams = new LayoutParams(SettingConfig.getResolutionValue(62),
//                LayoutParams.MATCH_PARENT);
//        valueTv.setLayoutParams(valueTvParams);
//        valueTv.setGravity(Gravity.CENTER );
//        valueTv.setTextColor(getResources().getColor(R.color.t_2c));
//        valueTv.setTextSize(SettingConfig.getDpiValue(isShort?28:32));
//        realContentLayout.addView(valueTv);
//
//         layerDrawable = (LayerDrawable) seekbar.getProgressDrawable();
//         proDrawable = layerDrawable.getDrawable(2);
//
//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                touchProgressChange();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }
//
//    @Override
//    public void updateUI()
//    {
//        if (settingItemData != null)
//        {
//            TCRangeSetData rangeData = (TCRangeSetData) settingItemData.setData;
//            if (rangeData != null)
//            {
//                max = rangeData.getMax();
//                min = rangeData.getMin();
//                current = rangeData.getCurrent();
//                if (min < 0)
//                { // 对于 -10 ~ +10 的进度类型，转型为 0 ~ 20
//                    offset = Math.abs(min);
//                    min = 0;
//                    max = max + offset;
//                    current += offset;
//                }
//                seekbar.setMax(max);
//                seekbar.setProgress(current);
//                valueTv.setText(String.valueOf(getUICurrentValue()));
//            } else
//            {
//                Debugger.e("range data is null");
//            }
//        }
//    }
//
//    // 减去偏移量
//    private int getUICurrentValue()
//    {
//        return current - offset;
//    }
//
//    @Override
//    public void setIsShort(boolean isShort)
//    {
//        super.setIsShort(isShort);
//        if (isShort)
//        {
//            valueTv.setTextSize(SettingConfig.getDpiValue(SkyTextSize.t_3));
//            LayoutParams progressbarParams = new LayoutParams(SettingConfig.getResolutionValue(366-28),
//                    SettingConfig.getResolutionValue(80));
//            seekbar.setPadding(SettingConfig.getResolutionValue(38),0,SettingConfig.getResolutionValue(18),0);
//            seekbar.setLayoutParams(progressbarParams);
//
//        } else
//        {
//            valueTv.setTextSize(SettingConfig.getDpiValue(SkyTextSize.t_4));
//            // seekbar.setProgressDrawable(getResources().getDrawable(R.drawable.setting_item_seekbar_progress_bg_unfocus));
//        }
////        seekbar.setProgressDrawable(getResources()
////                .getDrawable(R.drawable.setting_item_seekbar_progress_bg_short_unfocus));
//
//        initSeekbar();
//    }
//
//    private void initSeekbar()
//    {
//    }
//
//    private void changeProColor(boolean hasFocus)
//    {
//        Rect thumb = seekbar.getThumb().getBounds();
//
//        if (hasFocus)
//        {
//            Debugger.d("proDrawable "+layerDrawable.getIntrinsicHeight());
//            seekbar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_setting_focus));
////            proDrawable.setColorFilter(getResources().getColor(R.color.c_5b), PorterDuff.Mode.SRC);
//            Drawable drawable = getResources().getDrawable(R.drawable.seekbar_thumb_unfocus);
//            int intrinsicHeight = drawable.getIntrinsicHeight();
//            int intrinsicWidth = drawable.getIntrinsicWidth();
//            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
//                //竖屏下获取到图片跟横屏大小一致 需转小
//                Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                        : Bitmap.Config.RGB_565;
//                Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth,intrinsicHeight, config);
//                Canvas canvas = new Canvas(bitmap);
//                drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
//                drawable.draw(canvas);
//                drawable = new BitmapDrawable(bitmap);
//                Debugger.d("proDrawable "+drawable.getIntrinsicWidth()+"__"+drawable.getIntrinsicHeight());
//            }
//            seekbar.setThumb(drawable);
//        } else
//        {
//            seekbar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_setting));
////            proDrawable.setColorFilter(getResources().getColor(R.color.c_4b), PorterDuff.Mode.SRC);
//            Drawable drawable = getResources().getDrawable(R.drawable.thumb_setting);
//            drawable.setBounds(thumb);
//            seekbar.setThumb(drawable);
//        }
//    }
//    @Override
//    public boolean onKeyRight()
//    {
//        if (current < max)
//        {
//            isManualModify = true;
//            current++;
//            seekbar.setProgress(current);
//            valueTv.setText(String.valueOf(getUICurrentValue()));
//            TCRangeSetData rangeData = (TCRangeSetData) settingItemData.setData;
//            rangeData.setCurrent(getUICurrentValue());
////            set(true);
//            keyDataChange();
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onKeyLeft()
//    {
//        if (current > min)
//        {
//            isManualModify = true;
//            current--;
//            seekbar.setProgress(current);
//            valueTv.setText(String.valueOf(getUICurrentValue()));
//            TCRangeSetData rangeData = (TCRangeSetData) settingItemData.setData;
//            rangeData.setCurrent(getUICurrentValue());
////            set(true);
//            keyDataChange();
//        }
//        return true;
//    }
//    private void keyDataChange(){
//        if (lastDataChangeTime==0||lastDataChangeTime+300<SystemClock.uptimeMillis()){
//            lastDataChangeTime=SystemClock.uptimeMillis();
//            set(true);
//            return;
//        }else {
//            handler.removeCallbacks(delayRunnable);
//        }
//        handler.postDelayed(delayRunnable,300);
//    }
//    private void touchProgressChange(){
//        if (!isEnabled()){
//            return;
//        }
//        int temp=seekbar.getProgress();
//        if (temp==current){
//            return;
//        }
//        isManualModify = true;
//        current=temp;
//        valueTv.setText(String.valueOf(getUICurrentValue()));
//        TCRangeSetData rangeData = (TCRangeSetData) settingItemData.setData;
//        rangeData.setCurrent(getUICurrentValue());
//        set(true);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode)
//    {
//        switch (keyCode)
//        {
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                set(false);
//        }
//        return super.onKeyUp(keyCode);
//    }
//
//    private void set(boolean needDelay)
//    {
//        if (listener != null)
//        {
//            listener.onSettingItemDataChanged(settingItemData, needDelay, isManualModify);
//        }
//    }
//
//    @Override
//    public void obtainFocus()
//    {
//        haseFocuse=true;
//        super.obtainFocus();
//        changeProColor(true);
//        Debugger.d("obtainFocus  Range");
//        valueTv.setTextColor(getResources().getColor(R.color.t_1a));
//    }
//
//    @Override
//    public void loseFocus()
//    {
//        haseFocuse=false;
//        super.loseFocus();
//        changeProColor(false);
//        valueTv.setTextColor(getResources().getColor(R.color.t_2c));
//    }
//
//    @Override
//    public void disable()
//    {
//        haseFocuse=false;
//        super.disable();
//        this.setEnabled(false);
//        this.setFocusable(false);
//        this.setFocusableInTouchMode(false);
//        seekbar.setFocusable(false);
//        seekbar.setClickable(false);
//        seekbar.setEnabled(false);
//        seekbar.setSelected(false);
//        valueTv.setTextColor(getResources().getColor(R.color.t_2d));
//    }
//
//    @Override
//    public void enable()
//    {
//        super.enable();
//        this.setEnabled(true);
//        this.setFocusable(true);
//        this.setFocusableInTouchMode(true);
//        seekbar.setFocusable(true);
//        seekbar.setClickable(true);
//        seekbar.setEnabled(true);
//        seekbar.setSelected(true);
//        valueTv.setTextColor(getResources().getColor(haseFocuse?R.color.t_1a:R.color.t_2c));
//    }
//}
