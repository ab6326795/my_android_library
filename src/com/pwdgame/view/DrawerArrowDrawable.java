package com.pwdgame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.pwdgame.library.R;

public class DrawerArrowDrawable extends Drawable {
    private static final float ARROW_HEAD_ANGLE = (float) Math.toRadians(45.0D);
    /**
     * 上下间隔
     */
    protected float mBarGap;
    protected float mBarSize;
    /**
     * 画笔粗细
     */
    protected float mBarThickness;
    protected float mMiddleArrowSize;
    protected final Paint mPaint = new Paint();
    protected final Path mPath = new Path();
    protected float mProgress;
    protected int mSize;
    protected float mVerticalMirror = 1f;
    protected float mTopBottomArrowSize;
    protected Context context;

    public DrawerArrowDrawable(Context context) {
        this.context = context;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(context.getResources().getColor(R.color.ldrawer_color));
        this.mSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_drawableSize);
        this.mBarSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_barSize);
        this.mTopBottomArrowSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_topBottomBarArrowSize);
        this.mBarThickness = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_thickness);
        this.mBarGap = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_gapBetweenBars);
        this.mMiddleArrowSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_middleBarArrowSize);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
        this.mPaint.setStrokeWidth(this.mBarThickness);
    }

    protected float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
        return paramFloat1 + paramFloat3 * (paramFloat2 - paramFloat1);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect localRect = getBounds();
        float f1 = lerp(this.mBarSize, this.mTopBottomArrowSize, this.mProgress);
        float f2 = lerp(this.mBarSize, this.mMiddleArrowSize, this.mProgress);
        float f3 = lerp(0.0F, this.mBarThickness / 2.0F, this.mProgress);
        float f4 = lerp(0.0F, ARROW_HEAD_ANGLE, this.mProgress);
        float f5 = 0.0F;
        float f6 = 180.0F;
        float f7 = lerp(f5, f6, this.mProgress);
        float f8 = lerp(this.mBarGap + this.mBarThickness, 0.0F, this.mProgress);
        this.mPath.rewind();
        float f9 = -f2 / 2.0F;
        this.mPath.moveTo(f9 + f3, 0.0F);
        this.mPath.rLineTo(f2 - f3, 0.0F);
        float f10 = (float) Math.round(f1 * Math.cos(f4));
        float f11 = (float) Math.round(f1 * Math.sin(f4));
        this.mPath.moveTo(f9, f8);
        this.mPath.rLineTo(f10, f11);
        this.mPath.moveTo(f9, -f8);
        this.mPath.rLineTo(f10, -f11);
        this.mPath.moveTo(0.0F, 0.0F);
        this.mPath.close();
        canvas.save();
        if (!isLayoutRtl())
            canvas.rotate(180.0F, localRect.centerX(), localRect.centerY());
        canvas.rotate(f7 * mVerticalMirror, localRect.centerX(), localRect.centerY());
        canvas.translate(localRect.centerX(), localRect.centerY());
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restore();
    }

    public int getIntrinsicHeight() {
        return this.mSize;
    }

    public int getIntrinsicWidth() {
        return this.mSize;
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * Android现在提供了一些API，使您可以构建更优雅的变换布局方向的用户界面。这些界面将支持由右到左（RTL）的语言和阅读方向。比如阿拉伯语和希伯来语。
     * 为了使您的应用支持RTL布局，需要在manifest文件中设置元素的android:supportsRtl属性为“true”。一旦启用，该系统将启用各种RTL API来
     * 用RTL布局显示您的应用。举例来说，在操作栏中将操作按钮显示在左侧，而把图标和标题显示在右侧。所有您用框架提供的View类所构建的布局也会以相反的方向显示。
     * @return
     */
    public boolean isLayoutRtl() {
        return false;
    }
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public void setVerticalMirror(boolean mVerticalMirror) {
        this.mVerticalMirror = mVerticalMirror ? 1 : -1;
    }

    public void setProgress(float paramFloat) {
        this.mProgress = paramFloat;
        invalidateSelf();
    }

    public void setColor(int resourceId) {
        this.mPaint.setColor(context.getResources().getColor(resourceId));
    }
}