package com.ypf.study.myrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class MyRecyclerView extends ViewGroup {
    private MyAdapter adapter;
    private MyRecyclerViewPool recyclerViewPool;
    private boolean needlayout;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (recyclerViewPool == null) {
            recyclerViewPool = new MyRecyclerViewPool();
        }
    }

    public void setAdapter(MyAdapter adapter) {
        this.adapter = adapter;
        needlayout = true;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean change, int left, int top, int right, int bottom) {
        if (needlayout || change) {
            needlayout = false;
        }
    }

    static abstract class MyAdapter<VH extends MyViewHolder> {
        abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

        abstract void onBindViewHolder(@NonNull VH holder, int position);

        abstract int getItemCount();
    }
}
