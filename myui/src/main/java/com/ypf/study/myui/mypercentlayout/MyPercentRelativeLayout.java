package com.ypf.study.myui.mypercentlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ypf.study.myui.R;

/**
 * 自定义百分比布局
 */
public class MyPercentRelativeLayout extends RelativeLayout {
    public MyPercentRelativeLayout(Context context) {
        super(context);
    }

    public MyPercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPercentRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 将百分比换算为真实大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getLayoutParams() instanceof LayoutParams) {
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                layoutParams.width = (int) (layoutParams.widthPercent * widthSize);
                layoutParams.height = (int) (layoutParams.heightPercent * heightSize);
                layoutParams.leftMargin = (int) (layoutParams.marginLeftPercent * widthSize);
                layoutParams.rightMargin = (int) (layoutParams.marginRightPercent * widthSize);
                layoutParams.topMargin = (int) (layoutParams.marginTopPercent * heightSize);
                layoutParams.bottomMargin = (int) (layoutParams.marginBottomPercent * heightSize);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 生成LayoutParams对象，在onMeasure()方法之前调用
     1   调用目的 是为了给子控件生成LayoutParams   赋值给子控件    view.getLayoutParams
     View  --> generateLayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    static class LayoutParams extends RelativeLayout.LayoutParams {
        private float widthPercent;
        private float heightPercent;
        private float marginLeftPercent;
        private float marginRightPercent;
        private float marginTopPercent;
        private float marginBottomPercent;

        /**
         * app:widthPercent="0.5"
         * app:heightPercent="0.5"
         * app:marginLeftPercent="0.25"
         * app:marginTopPercent="0.25"
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //将attrs转换为TypedArray
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MyPercentRelativeLayout);
            widthPercent = a.getFloat(R.styleable.MyPercentRelativeLayout_widthPercent, 0);
            heightPercent = a.getFloat(R.styleable.MyPercentRelativeLayout_heightPercent, 0);
            marginLeftPercent = a.getFloat(R.styleable.MyPercentRelativeLayout_marginLeftPercent, 0);
            marginRightPercent = a.getFloat(R.styleable.MyPercentRelativeLayout_marginRightPercent, 0);
            marginTopPercent = a.getFloat(R.styleable.MyPercentRelativeLayout_marginTopPercent, 0);
            marginBottomPercent = a.getFloat(R.styleable.MyPercentRelativeLayout_marginBottomPercent, 0);
            a.recycle();//将a进行回收
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
