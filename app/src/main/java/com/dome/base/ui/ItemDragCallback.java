package com.dome.base.ui;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 长按可拖动实现效果
 * 一般和 SwipeItemLayout 使用(可侧滑可长按)
 */
public class ItemDragCallback extends ItemTouchHelper.Callback {
    private ItemDragLinstener mItemDragLinstener;
    private boolean dragFlag = true;  //是否是可拖动标志

    public static void attachToRecyclerView(RecyclerView recyclerView, ItemDragLinstener itemDragLinstener) {
        ItemDragCallback itemDragCallback = new ItemDragCallback(itemDragLinstener);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public ItemDragCallback(ItemDragLinstener itemDragLinstener) {
        mItemDragLinstener = itemDragLinstener;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //是否是编辑状态 --- 这里可以写那些条目允许条目拖动
        if (viewHolder.getAdapterPosition() == 0) {
            //例如第一条item不可移动 return 0;
        }
        if (!dragFlag) {
            return 0;
        }
        int dragFlag = 0;
        int swipeFlag = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                    | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
            swipeFlag = 0;
        } else {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
            swipeFlag = ItemTouchHelper.END; //控制侧滑
            swipeFlag = 0; //不允许侧滑
        }
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();   //拖动的position
        int toPosition = target.getAdapterPosition();     //释放的position

        if (mItemDragLinstener != null) {
            Collections.swap(mItemDragLinstener.getData(), fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemRangeChanged(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //长按时调用
            if (mItemDragLinstener != null) {
                mItemDragLinstener.onDraging(viewHolder);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //松手时调用
        if (mItemDragLinstener != null) {
            mItemDragLinstener.onDragCompletion(viewHolder);
        }
    }

    interface ItemDragLinstener {
        List getData();//获取adapter的数据源

        void onDraging(RecyclerView.ViewHolder viewHolder);//正在滑动的viewholder可以设置不一样样式,容易区分

        void onDragCompletion(RecyclerView.ViewHolder viewHolder);//滑动完成时
    }
}