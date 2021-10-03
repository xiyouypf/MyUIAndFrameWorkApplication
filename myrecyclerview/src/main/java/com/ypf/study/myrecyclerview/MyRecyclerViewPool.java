package com.ypf.study.myrecyclerview;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewPool {
    private static final int mMaxScrap = 5;
    static class ScrapData {
        final List<MyViewHolder> mScrapHeap = new ArrayList<>();
    }

    SparseArray<ScrapData> mScrap = new SparseArray<>();

    //根据viewType获取ViewHolder
    public MyViewHolder getRecyclerView(int viewType) {
        ScrapData scrapData = mScrap.get(viewType);
        if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
            List<MyViewHolder> scrapHeap = scrapData.mScrapHeap;
            for (int i = scrapHeap.size() - 1; i > 0; i--) {
                return scrapHeap.remove(i);
            }
        }
        return null;
    }

    //将ViewHolder放入回收池中
    public void putRecyclerView(MyViewHolder scrap) {
        int viewType = scrap.getItemViewType();
        List<MyViewHolder> scrapHeap = getScrapDataForType(viewType).mScrapHeap;
        if (scrapHeap.size() >= mMaxScrap) {
            return;
        }
        scrapHeap.add(scrap);
    }

    private ScrapData getScrapDataForType(int viewType) {
        ScrapData scrapData = mScrap.get(viewType);
        if (scrapData == null) {
            scrapData = new ScrapData();
            mScrap.put(viewType, scrapData);
        }
        return scrapData;
    }
}
