package com.ypf.study.myui.myflowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义流式布局
 */
public class MyFlowLayout extends ViewGroup {
    private static final String TAG = "MyFlowLayout";
    //存储所有的子控件
    private List<List<View>> list = new ArrayList<>();
    //存储每行子控件的最大高度
    private List<Integer> listLineHeight = new ArrayList<>();
    //防止多次测量
    private boolean isMeasure = false;

    public MyFlowLayout(Context context) {
        super(context);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //当前容器可用大小的参考值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //当前容器的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //容器总宽高
        int listTotalWidth = 0;
        int listTotalHeight = 0;
        if (!isMeasure) {
            isMeasure = true;
        } else {
            //当前行的宽高
            int currLineWidht = 0;
            int currLineHeight = 0;
            //当前控件的宽高
            int currWidth = 0;
            int currHeight = 0;
            //存储每一行的View
            List<View> lineViews = new ArrayList<>();
            //得到子View的总数量
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childAt.getLayoutParams();
                currWidth = childAt.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                currHeight = childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                if (currLineWidht + currWidth > widthSize) {//换行
                    listTotalWidth = Math.max(listTotalWidth, currLineWidht);
                    listTotalHeight += currLineHeight;
                    list.add(lineViews);
                    listLineHeight.add(currLineHeight);
                    //重置操作
                    lineViews = new ArrayList<>();
                    currLineWidht = currWidth;
                    currLineHeight = currHeight;
                    lineViews.add(childAt);
                } else {
                    currLineWidht += currWidth;
                    currLineHeight = Math.max(currLineHeight, currHeight);
                    lineViews.add(childAt);
                }
                if (i == childCount - 1) {
                    listTotalWidth = Math.max(listTotalWidth, currLineWidht);
                    listTotalHeight += currLineHeight;
                    list.add(lineViews);
                    listLineHeight.add(currLineHeight);
                }
            }
            Log.e(TAG, "onMeasure: " + list.size() + "");
        }
        int measureWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : listTotalWidth;
        int measureHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : listTotalHeight;
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean change, int left, int top, int right, int bottom) {
        int l, t, r, b;
        int startLeft = 0;
        int startTop = 0;
        for (List<View> views : list) {
            for (View view : views) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                l = startLeft;
                t = startTop;
                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMeasuredHeight();
                r = startLeft + viewWidth;
                b = startTop + viewHeight;
                view.layout(l, t, r, b);
                startLeft += viewWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            startLeft = 0;
            int i = list.indexOf(views);
            startTop += listLineHeight.get(i);
        }
        list.clear();
        listLineHeight.clear();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}