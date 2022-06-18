package com.dome.base.ui.recycler_leftslide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zw_engineering.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class DomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dome);
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerViewMatchLeftSlide.AdapterMatchLeftSlide<MyHolder>{
        @Override
        protected MyHolder onMatchLeftSlideCreateViewHolder(ViewGroup viewGroup, int type) {
            MyHolder myHolder = new MyHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_left_slide_item,viewGroup,false));
            return myHolder;
        }
        @Override
        protected void bindMatchLeftSlideViewHolder(MyHolder viewHolder, int position) {
        }
        @Override
        public int getItemCount() {
            return 50;
        }

    }
    static class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}