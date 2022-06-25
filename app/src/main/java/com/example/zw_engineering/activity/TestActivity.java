package com.example.zw_engineering.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zw_engineering.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<String> list = new ArrayList<>();
        list.add("Test");
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        Adapter adapter = new Adapter(android.R.layout.simple_list_item_1, list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        ToastUtils.showShort("Test");
                        break;
                }
            }
        });
    }

    class Adapter extends BaseQuickAdapter<String, MyHolder> {
        public Adapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull @NotNull MyHolder helper, String item) {
            helper.mTextView.setText(item);
        }
    }

    static class MyHolder extends BaseViewHolder {
        TextView mTextView;

        public MyHolder(View view) {
            super(view);
            mTextView = view.findViewById(android.R.id.text1);

        }
    }
}