package com._basebase.base.ui.wheelview;

import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.zw_engineering.R;
//资源文件夹配置
//<color name="wheel_divider_color">#80808080</color>
//<color name="wheel_highlight_color">#00000000</color>
//<color name="wheel_selected_text_color">#1db0b8</color>
//<color name="wheel_text_color">#727272</color>
//<declare-styleable name="Wheel3DView"><attr format="enum" name="wheelToward"><enum name="none" value="0"/><enum name="left" value="-1"/><enum name="right" value="1"/></attr></declare-styleable>
//<declare-styleable name="WheelView"><attr format="boolean" name="wheelCyclic"/><attr format="reference" name="wheelEntries"/><attr format="integer" name="wheelItemCount"/><attr format="dimension" name="wheelItemWidth"/><attr format="dimension" name="wheelItemHeight"/><attr format="dimension" name="wheelTextSize"/><attr format="color" name="wheelTextColor"/><attr format="color" name="wheelSelectedTextColor"/><attr format="color" name="wheelDividerColor"/><attr format="color" name="wheelHighlightColor"/></declare-styleable>
//<dimen name="wheel_divider_height">1dp</dimen>
//<dimen name="wheel_item_height">40dp</dimen>
//<dimen name="wheel_item_width">160dp</dimen>
//<dimen name="wheel_text_size">20sp</dimen>
//<string name="app_name">Library</string>

//xml文件下使用
//<com._basebase.base.ui.wheelview.WheelView
//        android:layout_centerInParent="true"
//        android:id="@+id/wheel3d"
//        android:background="#3f0f"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_marginLeft="20dp"
//        app:wheelCyclic="false"
//        app:wheelEntries="@array/default_array"
//        app:wheelItemCount="5"
//        app:wheelItemWidth="160dp"
//        app:wheelItemHeight="50dp"
//        app:wheelTextSize="@dimen/wheel_text_size"
//        app:wheelTextColor="@color/wheel_text_color"
//        app:wheelSelectedTextColor="@color/wheel_selected_text_color"
//        app:wheelDividerColor="@color/wheel_divider_color"
//        app:wheelHighlightColor="@color/wheel_highlight_color" />


class Dome {
    public void test(){
        WheelView wheelView = (WheelView) ActivityUtils.getTopActivity().findViewById(R.id.wheel3d);
        wheelView.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                CharSequence text = view.getItem(newIndex);
                Log.i("WheelView", String.format("index: %d, text: %s", newIndex, text));
            }
        });
        wheelView.setSelectBoldText(true);
        wheelView.setTextSize(14*3,20*3);
    }
}
