package com.example.zw_engineering;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com._basebase.base.ui.wheelview.OnWheelChangedListener;
import com._basebase.base.ui.wheelview.WheelView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDragUseActivity extends AppCompatActivity {

    private String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        List<String> mData = generateData(50);
        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "drag start");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                Log.d(TAG, "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "drag end");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
//                holder.setTextColor(R.id.tv, Color.BLACK);
            }
        };
        BaseItemDraggableAdapter<String, BaseViewHolder> draggableAdapter = new BaseItemDraggableAdapter<String, BaseViewHolder>(R.layout.item_drag,mData) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, String item) {
                helper.setText(R.id.tv, item +"");
            }
        };
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(draggableAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);//绑定recyclerview
        //可以不用设置,默认就是这个
        itemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        draggableAdapter.enableDragItem(itemTouchHelper);
        draggableAdapter.setOnItemDragListener(listener);

        mRecyclerView.setAdapter(draggableAdapter);

        WheelView wheelView = (WheelView) findViewById(R.id.wheel3d);
        wheelView.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                CharSequence text = view.getItem(newIndex);
                Log.i("WheelView", String.format("index: %d, text: %s", newIndex, text));
            }
        });
        wheelView.setSelectBoldText(true);
        wheelView.setTextSize(14*3,20*3);


        OptionsPickerView optionsPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                if (selectOption!=null){
//                    selectOption.select(options1);
//                }
            }
        }).build();
        optionsPickerView.setPicker(Arrays.asList("args","21asdfa","asdfasdasdfasdasdfasdasdfasdasdfasdasdfasdasdfasdasdfasdasdfasdasdfasdasdfasdend"));
        optionsPickerView.show();
    }

    private List<String> generateData(int i) {
        ArrayList<String> strings = new ArrayList<>(i);
        for (int j = 0; j < i; j++) {
            strings.add(String.valueOf(j));
        }
        return strings;
    }
}
