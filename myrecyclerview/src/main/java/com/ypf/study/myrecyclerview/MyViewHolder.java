package com.ypf.study.myrecyclerview;

import android.view.View;

public class MyViewHolder {
    //跟布局
    public View mItemView;
    private int mItemViewType = -1;

    public MyViewHolder(View itemView) {
        this.mItemView = itemView;
    }

    public int getItemViewType() {
        return mItemViewType;
    }

    public void setItemViewType(int mItemViewType) {
        this.mItemViewType = mItemViewType;
    }
}
