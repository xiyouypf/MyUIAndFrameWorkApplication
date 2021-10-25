package com.ypf.study.myui.springback;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 为ViewPager2提供上拉和下拉回弹效果,仅供ViewPager2使用，其它子控件均不支持
 */

public class SpringBackViewGroupForViewPager2 extends ConstraintLayout {
    private ViewPager2 viewPager;

    private int touchSlop;//最小滑动距离
    private int mCurrentY;//手指按下时的Y轴位置
    private int mCurrentTempY;//手指滑动到任意位置的Y轴位置
    private int diffY;//同一个事件序列中，当前滑动和上次滑动的距离
    private int finalDiffY;//从手指按下到手指滑动到任意位置的Y轴距离
    private int maxMoveHeight;//最大可以上拉或下拉的高度
    private boolean needLayout = true;
    ObjectAnimator scrollYAnim;//回弹属性动画
    private int stopVal;//属性动画被打断时的值

    public SpringBackViewGroupForViewPager2(Context context) {
        super(context);
    }

    public SpringBackViewGroupForViewPager2(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        maxMoveHeight = dp2px(context, 200);
    }

    public SpringBackViewGroupForViewPager2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        super.onLayout(change, l, t, r, b);
        if (viewPager == null) {
            viewPager = (ViewPager2) getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mCurrentY = (int) ev.getY();
            mCurrentTempY = mCurrentY;
            intercept = false;
            stopVal = stopAnim();
            if (viewPager.getCurrentItem() == 0 && ev.getY() <= viewPager.getScrollY()) {
                intercept = true;
            } else if (viewPager.getCurrentItem() == viewPager.getAdapter().getItemCount() - 1 && ev.getY() >= getHeight() + viewPager.getScrollY()) {
                intercept = true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int y2 = (int) (mCurrentY - ev.getY());
            if (Math.abs(y2) > touchSlop) {
                if ((viewPager.getCurrentItem() == 0 && y2 < 0)) {
                    intercept = true;
                } else if (viewPager.getCurrentItem() == 0 && y2 > 0 && lastAnimIsRunning()) {
                    viewPager.scrollTo(0, 0);
                } else if ((viewPager.getCurrentItem() == viewPager.getAdapter().getItemCount() - 1 && y2 > 0)) {
                    intercept = true;
                } else if (viewPager.getCurrentItem() == viewPager.getAdapter().getItemCount() - 1 && y2 < 0 && lastAnimIsRunning()) {
                    viewPager.scrollTo(0, 0);
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            intercept = false;
            startAnim(stopVal);
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startAnim(stopVal);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            diffY = (int) (mCurrentTempY - event.getY());
            finalDiffY = (int) (mCurrentY - event.getY()) + stopVal;
            if (finalDiffY < 0 && finalDiffY <= -maxMoveHeight) {
                finalDiffY = -maxMoveHeight;
                mCurrentTempY = diffY;
            } else if (finalDiffY > 0 && finalDiffY >= maxMoveHeight) {
                finalDiffY = maxMoveHeight;
                mCurrentTempY = finalDiffY;
            } else {
                viewPager.scrollBy(0, diffY);
                mCurrentTempY = (int) event.getY();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            startAnim(finalDiffY);
        }
        return super.onTouchEvent(event);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void startAnim(int diffY) {
        if (scrollYAnim != null && lastAnimIsRunning()) {
            scrollYAnim.cancel();
        }
        scrollYAnim = ObjectAnimator.ofInt(viewPager, "ScrollY", diffY, 0);
        scrollYAnim.setDuration((long) (700 * (Math.abs((float) diffY) / maxMoveHeight)));
        scrollYAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scrollYAnim.start();
    }

    private int stopAnim() {
        int lastScrollY = 0;
        if (scrollYAnim != null && scrollYAnim.isRunning()) {
            scrollYAnim.cancel();
            lastScrollY = (int) scrollYAnim.getAnimatedValue("ScrollY");
        }
        return lastScrollY;
    }

    private boolean lastAnimIsRunning() {
        if (scrollYAnim != null) {
            return !scrollYAnim.isPaused();
        }
        return false;
    }

}
