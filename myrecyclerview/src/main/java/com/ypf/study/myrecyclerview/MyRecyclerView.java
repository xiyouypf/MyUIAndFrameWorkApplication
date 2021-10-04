package com.ypf.study.myrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

//协调
public class MyRecyclerView extends ViewGroup {
    private static final String TAG = "MyRecyclerView";
    //适配器和回收池角色
    private MyAdapter adapter;
    private MyRecyclerViewPool recyclerViewPool;

    //当前RecyclerView容器的宽高
    private int width;
    private int height;

    //存储每一个item的高度
    private int heights[];
    //需要加载的数据行数
    private int rowCount;
    //缓存当前显示的itemView
    private List<MyViewHolder> viewList;

    //是否需要重新布局，控制onLayout初始化次数
    private boolean needlayout = true;
    //最小滑动距离
    private int touchSlop;
    //当前点击的y值
    private int currentY;
    //y偏移量 内容偏移量
    private int scrollY;
    //View的第一行 是内容行的多少行
    private int firstRow;
    //惯性滑动 使用速度
    private VelocityTracker velocityTracker;
    private int minmumVelocity;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (viewList == null) {
            viewList = new ArrayList<>();
        }
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.touchSlop = viewConfiguration.getScaledTouchSlop();//获取最小滑动距离
        this.minmumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();//获取最小速度
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(MyAdapter adapter) {
        this.adapter = adapter;
        if (recyclerViewPool == null) {
            recyclerViewPool = new MyRecyclerViewPool();
        }
        needlayout = true;
        requestLayout();
    }

    @Override//耗性能
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needlayout || changed) {
            needlayout = false;
            if (adapter != null) {
                width = r - l;
                height = b - t;
            }
            //摆放 每一个控件的宽高
            rowCount = adapter.getItemCount();
            heights = new int[rowCount];
            for (int i = 0; i < heights.length; i++) {
                heights[i] = adapter.getHeight();
            }
            //摆放
            int top = 0, bottom;
            for (int i = 0; i < rowCount && top < height; i++) {
                bottom = top + heights[i];
                //摆放View，生成View
                MyViewHolder viewHolder = makeAndStep(i, 0, top, width, bottom);
                viewList.add(viewHolder);
                //遍历条件
                top = bottom;
            }
        }
    }

    private MyViewHolder makeAndStep(int row, int left, int top, int right, int bottom) {
        //产生ViewHolder
        MyViewHolder viewHolder = obtainView(row, right - left, bottom - top);
        //摆放完成
        viewHolder.mItemView.layout(left, top, right, bottom);
        return viewHolder;
    }

    private MyViewHolder obtainView(int row, int width, int height) {
        int viewType = adapter.getItemViewType(row);
        MyViewHolder viewHolder = recyclerViewPool.getRecyclerView(viewType);
        if (viewHolder == null) {
            viewHolder = adapter.onCreateViewHolder(this, viewType);
        }
        //更新数据
        adapter.onBindViewHolder(viewHolder, row);
        viewHolder.setItemViewType(viewType);
        //进行测量
        viewHolder.mItemView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        addView(viewHolder.mItemView, 0);
        return viewHolder;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        //滑动
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int y2 = (int) event.getRawY();
            int diff = (int) (currentY - event.getRawY());
            scrollBy(0, diff);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //用户抬手，计算速度
            velocityTracker.computeCurrentVelocity(1000);
            int velocity = (int) velocityTracker.getYVelocity();
            int initY = scrollY + sumArray(heights, 1, firstRow);
            int maxY = Math.max(0, sumArray(heights, 0, heights.length) - height);
            if (Math.abs(velocity) > minmumVelocity) {
                new Flinger(getContext()).start(0, initY, 0, velocity, 0, maxY);
            }

        }
        return super.onTouchEvent(event);
    }

    class Flinger implements Runnable {
        //记录偏移量
        private int initY;
        private Scroller scroller;

        public Flinger(Context context) {
            this.scroller = new Scroller(context);
        }

        void start(int initX, int initY, int initialVelocityX, int initialVelocityY, int maxX, int maxY) {
            scroller.fling(initX, initY, initialVelocityX, initialVelocityY, 0, maxX, 0, maxY);
            this.initY = initY;
            post(this);
        }
        @Override
        public void run() {
            if (scroller.isFinished()) {
                return;
            }
            boolean more = scroller.computeScrollOffset();
            //滑动一点点
            int y = scroller.getCurrY();
            int diff = initY - y;
            if (diff != 0) {
                scrollBy(0, diff);
            }
            if (more) {
                post(this);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            currentY = (int) ev.getRawY();
            Log.d(TAG, "onInterceptTouchEvent: " + "ActionDown");
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int y2 = (int) Math.abs(currentY - ev.getRawY());
            if (y2 > touchSlop) {
                intercept = true;
            }
            Log.d(TAG, "onInterceptTouchEvent: " + "ActionMove");
        }
        return intercept;
    }

    //只对canvas进行滚动
    //需要重新摆放每一个子控件
    @Override
    public void scrollBy(int x, int y) {
        //摆放
        scrollY += y;
        scrollBounds();
        if (scrollY > 0) {//上滑
            while (scrollY > heights[firstRow]) {
                //扔到回收池 firstRow++
                if (!viewList.isEmpty()) {
                    removeView(viewList.remove(0));
                }
                scrollY -= heights[firstRow];
                firstRow++;
                Log.d(TAG, "scrollBy: " + "上滑移除一个元素");
            }
            while (getFilledHeight() < height) {
                //需要被添加 滑动 肯定不是第一屏数据
                int dataIndex = firstRow + viewList.size();
                MyViewHolder viewHolder = obtainView(dataIndex, width, heights[dataIndex]);
                viewList.add(viewList.size(), viewHolder);
                Log.d(TAG, "scrollBy: " + "上滑添加一个元素");
            }
        } else {//下滑
            while (scrollY < 0) {
                firstRow--;
                obtainView(firstRow, width, heights[firstRow]);
                scrollY += heights[firstRow];
                Log.d(TAG, "scrollBy: " + "下滑添加一个元素");
            }
            while (!viewList.isEmpty() && getFilledHeight() - heights[firstRow + viewList.size()] >= height) {
                removeView(viewList.remove(viewList.size() - 1));
                Log.d(TAG, "scrollBy: " + "下滑移除一个元素");
            }
        }
        repositionViews();
    }

    public void scrollBounds() {
        if (scrollY < 0) {//下滑极限值
            //scrollY=0 正常下滑时，不会影响下滑，在下滑到了极限值 scrollY=0
            if (scrollY > -sumArray(heights, 0, firstRow)) {
                scrollY = scrollY;
            } else {
                scrollY = -sumArray(heights, 0, firstRow);
            }
//            scrollY = Math.max(scrollY, -sumArray(heights, 0, firstRow));
        } else {//下滑极限值
            if (sumArray(heights, firstRow, heights.length - firstRow) - scrollY < height) {
                scrollY = sumArray(heights, firstRow, heights.length - firstRow) - height;
            }
        }
    }
    private void repositionViews() {
        int top, bottom, i = 0;
        top = -scrollY;
        for (MyViewHolder viewHolder : viewList) {
            i++;
            bottom = top + heights[i];
            viewHolder.mItemView.layout(0, top, width, bottom);
            //循环条件
            top = bottom;
        }
    }

    private int getFilledHeight() {
        return sumArray(heights, firstRow, viewList.size()) - scrollY;
    }

    private int sumArray(int[] arr, int firstIndex, int count) {
        int sum = 0;
        for (int i = firstIndex; i < firstIndex + count; i++) {
            sum += arr[i];
        }
        return sum;
    }

    private void removeView(MyViewHolder remove) {
        recyclerViewPool.putRecyclerView(remove);
        removeView(remove.mItemView);
    }

    //适配器角色
    static abstract class MyAdapter<VH extends MyViewHolder> {
        //创建Viewholder
        abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

        //绑定ViewHolder
        abstract void onBindViewHolder(@NonNull VH holder, int position);

        //item的类型
        public int getItemViewType(int position) {
            return 0;
        }

        //item的数量
        abstract int getItemCount();

        //每一个item的高度
        abstract public int getHeight();
    }
}
