package com.ypf.study.myroom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.ParcelableSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.tencent.widget.immersive.ImmersiveUtils;

//import cooperation.qqcircle.helpers.QCircleSkinHelper;

public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int bgColor;
    private int textColor;

    public RoundBackgroundColorSpan(int bgColor, int textColor) {
//        super(bgColor);
        super();
//        this.bgColor = QCircleSkinHelper.getInstance().getColor(bgColor);
//        this.textColor = QCircleSkinHelper.getInstance().getColor(textColor);
        this.bgColor = bgColor;
        this.textColor = textColor;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        int originalColor = paint.getColor();
        int originalAlpha = paint.getAlpha();
        paint.setColor(this.bgColor);
        paint.setAlpha(51);
        //画圆角矩形背景
        canvas.drawRoundRect(new RectF(x,
                        top + dp2px(2),
                        x + ((int) paint.measureText(text, start, end) + dp2px(6)),
                        y + dp2px(3)),
                dp2px(2),
                dp2px(2),
                paint);
        paint.setColor(this.textColor);
        paint.setAlpha(originalAlpha);
        //画文字,两边各增加3dp
        canvas.drawText(text, start, end, x + dp2px(3), y - dp2px(1), paint);
        //将paint复原
        paint.setColor(originalColor);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        //设置宽度为文字宽度加6dp
        return ((int) paint.measureText(text, start, end) + dp2px(6));
    }

    private int dp2px(int dp) {
        return 5 * dp;
    }

//    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
        int originalColor = paint.getColor();
        int originalAlpha = paint.getAlpha();
        paint.setColor(this.bgColor);
        paint.setAlpha(51);
        //画圆角矩形背景
        canvas.drawRoundRect(new RectF(left,
                        top + dp2px(2),
                        left + ((int) paint.measureText(text, start, end) + dp2px(6)),
                        baseline + dp2px(3)),
                dp2px(2),
                dp2px(2),
                paint);
        paint.setColor(this.textColor);
        paint.setAlpha(originalAlpha);
        //画文字,两边各增加3dp
//        canvas.drawText(text, start, end, left + dp2px(3), baseline - dp2px(1), paint);
        //将paint复原
        paint.setColor(originalColor);
    }
}