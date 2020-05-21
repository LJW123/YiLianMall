package com.leshan.ylyj.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 底部凸起背景
 * Created by Administrator on 2018/1/21 0021.
 */

public class BulgeBgView extends View {
    private float mWitth;
    private float mHeight;
    private Paint mPaint;
    private Path mPath;
    private float leftHeight;
    private float bottomHeitht;
    private float maxHeight;
    private Context mContext;
    private int startColor = Color.WHITE; //左下
    private int endColor = Color.WHITE;//右上
    private int[] colorInt;

    public BulgeBgView(Context context) {
        super(context);
    }

    public BulgeBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
//        mPaint.setColor(Color.parseColor("#EB5154"));

        mPath = new Path();
    }

    public BulgeBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST) {
            mWitth = getPaddingLeft() + getPaddingRight();

        }
        if (hMode == MeasureSpec.AT_MOST) {
            mHeight = getPaddingBottom() + getPaddingTop();
        }
        mWitth = width;
        mHeight = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.moveTo(0, 0);
        mPath.lineTo(0, leftHeight);
        float x = mWitth / 2;
        mPath.cubicTo(0, leftHeight, x, maxHeight, mWitth, leftHeight);
        mPath.lineTo(mWitth, 0);
        mPath.close();
        float[] floats = new float[]{0, 0.5f};
        colorInt = new int[]{endColor, startColor};
        LinearGradient linearGradient = new LinearGradient(getMeasuredWidth(), 0, 0, getMeasuredHeight(), colorInt, floats, Shader.TileMode.MIRROR);
        mPaint.setShader(linearGradient);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public void setLeftHeight(float height) {
        leftHeight = dip2px(mContext, height);
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = dip2px(mContext, maxHeight);
    }

    /**
     * chong xin she zhi yin ying
     *
     * @param startColor
     * @param endColor
     */
    public void resetBg(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public void resetDrawg() {
        postInvalidate();
    }

}
