package com.ypf.study.myroom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int bgColor;
    private int textColor;

    public RoundBackgroundColorSpan(int bgColor, int textColor) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        int originalColor = paint.getColor();
        paint.setColor(this.bgColor);
        //画圆角矩形背景
        canvas.drawRoundRect(new RectF(x,
                        top + dp2px(3),
                        x + ((int) paint.measureText(text, start, end) + dp2px(16)),
                        bottom + dp2px(8)),
                dp2px(4),
                dp2px(4),
                paint);
        paint.setColor(this.textColor);
        //画文字,两边各增加8dp
        canvas.drawText(text, start, end, x + dp2px(8), y + dp2px(5), paint);
        //将paint复原
        paint.setColor(originalColor);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        //设置宽度为文字宽度加16dp
        return ((int) paint.measureText(text, start, end) + dp2px(16));
    }

    private int dp2px(int dp) {
        return 2 * dp;
    }

//    @Override
//    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
//        //设置宽度为文字宽度加16dp
//        return ((int)paint.measureText(text, start, end)+dp2px(16));
//    }

    //    @Override
//    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
//        int originalColor = paint.getColor();
//        paint.setColor(this.bgColor);
//        //画圆角矩形背景
//        canvas.drawRoundRect(new RectF(x,
//                        top+ dp2px(3),
//                        x + ((int) paint.measureText(text, start, end)+ dp2px(16)),
//                        bottom-dp2px(1)),
//
//                dp2px(4),
//                dp2px(4),
//                paint);
//        paint.setColor(this.textColor);
//        //画文字,两边各增加8dp
//        canvas.drawText(text, start, end, x+dp2px(8), y, paint);
//        //将paint复原
//        paint.setColor(originalColor);
//    }
}