package com.leshan.ylyj.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 爱心成长体系view
 * 左右边距由父控件进行设置
 * 设置柱子的高度集合，柱子的高度集合和上下部文字集合长度必须相同
 * Created by @author zhaiyaohua on 2018/1/10 0010.
 */

public class LoveLevelView extends View {
    private Context mContext;
    private int mWidth, mHeight;//总宽高
    private Paint mPaint;//柱子画笔
    private Paint mTopTextPaint;//顶部文字
    private Paint mBottomTextPaint;//底部文字画笔
    private Paint mRoundBgPaint;//顶部文字气泡画笔
    private float radius = 5;//柱子的圆角
    private float startSpacing;//左边的空间
    private float endSpacing;//右边的空间
    private String[] topTextArray = new String[]{"爱心新秀", "爱心见习生", "爱心达人", "爱心大使", "爱心元老", "爱心王者"};
    private Path path;//气泡下面三角路线
    private float[] mColumLenght = new float[]{23, 39, 54, 89, 124, 164};//柱子的高度集合
    private String[] botoomTextArray = new String[]{"Lv.0", "Lv.4", "Lv.7", "Lv.16", "Lv.23", "Lv.33"};//底部文字集合
    private float widthSpace = 0;//柱子的间隔
    private float topPading;//文字距离上下气泡距离
    private float leftPading;//文字距离气泡左右距离
    private int curLeve = 0;//当前的顶级
    private float topSize = 9f;
    private float butomSize = 10f;
    private float minFontWidth = 0;//顶部最小字体宽度
    private float maxFontWidth = 0;//顶部做大字体快读
    //顶部气泡圆角默认为2dp
    private float bubbleRadiu=3;

    public LoveLevelView(Context context) {
        super(context);

    }

    public LoveLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    public LoveLevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 绘制
     *
     * @param canvas
     * @param x      文字边框左上角的横坐标
     * @param y1     文字边框左上角纵坐标
     */
    private void drawText(Canvas canvas, float x, float y1, int i) {
        //获取文字的具体信息
        resetPaint(i);
        //绘制气泡和文字,测量文字的宽度
//        float stringWidth = mTopTextPaint.measureText(topTextArray[i]);
        //获取文字的绘制信息
        Paint.FontMetrics fontMetrics = mTopTextPaint.getFontMetrics();
        Rect rect = new Rect();
        mTopTextPaint.getTextBounds(topTextArray[i], 0, topTextArray[i].length(), rect);
        float boundsHeight = (rect.height()) / 2 + topPading;
        float y = boundsHeight + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2 + y1;
        //气泡边框绘制
        float dx=rect.width()-minFontWidth;
        RectF rectF = new RectF(x - leftPading-dx/2, y1, x + rect.width() -dx/2+ leftPading, 2 * boundsHeight + y1);
        canvas.drawRoundRect(rectF, dip2px(mContext, bubbleRadiu), dip2px(mContext, bubbleRadiu), mRoundBgPaint);
        canvas.drawText(topTextArray[i], x-dx/2, y, mTopTextPaint);

        //绘制柱子
        float xColumn = (rectF.right + rectF.left) / 2;
        float yColum = rectF.bottom + dip2px(mContext, 9f);
        RectF rectF1 = new RectF(xColumn - radius, yColum, xColumn + radius, yColum + 2 * radius);
        if (i <= curLeve - 1) {
            LinearGradient linearGradient = new LinearGradient(rectF1.left, rectF1.top, rectF1.right, yColum + dip2px(mContext, mColumLenght[i]), Color.parseColor("#FF7F47"), Color.parseColor("#FA5C16"), Shader.TileMode.MIRROR);
            mPaint.setShader(linearGradient);
        } else {
            mPaint.setColor(Color.parseColor("#DEDEDE"));
        }
        canvas.drawArc(rectF1, 0, 360, true, mPaint);
        mPaint.setStrokeWidth(2 * radius);
        canvas.drawLine(xColumn, yColum + radius, xColumn, yColum + dip2px(mContext, mColumLenght[i]) - radius, mPaint);
        //绘制倒是三角
        path.reset();
        path.moveTo(xColumn, rectF.bottom + dip2px(mContext, 3));
        path.lineTo(xColumn - dip2px(mContext, 3), rectF.bottom - 1);
        path.lineTo(xColumn + dip2px(mContext, 3), rectF.bottom - 1);
        path.close();
        canvas.drawPath(path, mRoundBgPaint);
        //绘制底部的文字
        if (i <= curLeve - 1) {
            mBottomTextPaint.setColor(Color.parseColor("#FA5D18"));
        } else {
            mBottomTextPaint.setColor(Color.parseColor("#999999"));
        }
        Paint.FontMetrics fontMetrics1 = mBottomTextPaint.getFontMetrics();
        Rect rec1 = new Rect();
        mBottomTextPaint.getTextBounds(botoomTextArray[i], 0, botoomTextArray[i].length(), rec1);
        float bottomTextY = yColum + dip2px(mContext, mColumLenght[i] + 10f) - radius + (Math.abs(fontMetrics1.ascent) - fontMetrics1.descent) / 2 + rec1.height() / 2;
        float bottomTextX = xColumn;
        canvas.drawText(botoomTextArray[i], bottomTextX, bottomTextY, mBottomTextPaint);
    }

    public void setWidthSpace(float widthSpace) {
        this.widthSpace = widthSpace;
        requestLayout();
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 500);
    }

    /**
     * 设置当前的顶级和顶部文字和底部的等级
     *
     * @param topString
     */
    public void setTopTextArray(String[] topString) {
        this.topTextArray = topString;
    }

    public void setBottomTextArray(String[] botoomTextArray) {
        this.botoomTextArray = botoomTextArray;
    }

    public void setCurLeve(int leve) {
        this.curLeve = leve;
    }

    /**
     * 设置柱子的高度集合，柱子的高度集合和上下部文字集合长度必须相同
     *
     * @param mColumLenght
     */
    public void setmColumLenght(float[] mColumLenght) {
        this.mColumLenght = mColumLenght;
    }

    /**
     * 设置柱子的半径--2倍半径就是柱子的宽度
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void resetDrawView() {
        postInvalidate();
    }

    /**
     * 设置顶部文字的尺寸
     *
     * @param topSize
     */
    public void setTopTextSize(float topSize) {
        this.topSize = topSize;
    }

    /**
     * 设置底部文字的尺寸
     *
     * @param butomSize
     */
    public void setButtomTextSize(float butomSize) {
        this.butomSize = butomSize;
    }

    /**
     * 设置顶部气泡圆角不能大于5
     * @param bubbleRadiu
     */
    public void setBubbleRadiu(float bubbleRadiu){
        this.bubbleRadiu=bubbleRadiu;

    }

    /**
     * 测量两个柱子之间的空间
     */

    private void measureSpacing() {
        float leftPading = dip2px(mContext, 4.5f);//左右边距
        float stringWidth = mTopTextPaint.measureText(topTextArray[0]);
        startSpacing = stringWidth / 2 + leftPading;
        endSpacing = mTopTextPaint.measureText(topTextArray[topTextArray.length - 1]) / 2 + 2*leftPading;
        widthSpace = (mWidth - startSpacing - endSpacing) / (topTextArray.length - 1);
    }

    /**
     * 重置画笔
     *
     * @param i
     */
    private void resetPaint(int i) {
        mPaint.reset();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#FA5D18"));
        mPaint.setAntiAlias(true);

        mRoundBgPaint.setColor(Color.parseColor("#00000000"));
        if (i < curLeve - 1) {
            mTopTextPaint.setColor(Color.parseColor("#FA5D18"));
        } else if (i == curLeve - 1) {
            mRoundBgPaint.setColor(Color.parseColor("#FF834C"));
            mTopTextPaint.setColor(Color.parseColor("#FFFFFF"));
        } else {
            mTopTextPaint.setColor(Color.parseColor("#CCCCCC"));
        }
    }

    /**
     * 初始化画笔和圆角信息和空间信息
     */
    private void init() {
        radius = dip2px(mContext, radius);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);

        mTopTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTopTextPaint.setTextSize(sp2px(mContext, topSize));
        Typeface font1 = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mTopTextPaint.setTypeface(font1);

        mBottomTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomTextPaint.setTextSize(sp2px(mContext, butomSize));
        Typeface font2 = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mBottomTextPaint.setTypeface(font2);
        mBottomTextPaint.setTextAlign(Paint.Align.CENTER);
        mBottomTextPaint.setColor(Color.parseColor("#FA5D18"));


        mRoundBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRoundBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        path = new Path();

        topPading = dip2px(mContext, 1.5f);//上下边距
        leftPading = dip2px(mContext, 4.5f);//左右边距
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureTopFontWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = (int) (getPaddingRight() + getPaddingLeft() + maxFontWidth * topTextArray.length+2*leftPading);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = (int) (getPaddingBottom() + getPaddingTop() + sp2px(mContext, topSize + butomSize) + dip2px(mContext, 20 + mColumLenght[mColumLenght.length - 1]));
        }
        measureSpacing();
        setMeasuredDimension(mWidth, mHeight);

    }

    /**
     * 测量顶部字体的宽度
     */
    private void measureTopFontWidth() {
        for (int i = 0; i < topTextArray.length; i++) {
            float width = mTopTextPaint.measureText(topTextArray[i]);
            if (minFontWidth == 0) {
                minFontWidth = width;
            }
            maxFontWidth = Math.max(maxFontWidth, width);
            minFontWidth = Math.min(minFontWidth, width);
        }
    }

    /**
     * 绘制控件
     *
     * @param canvas
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int length=Math.min(topTextArray.length,botoomTextArray.length);
        length=Math.min(length,mColumLenght.length);
        for (int i = 0; i < length; i++) {
            float y = dip2px(mContext, mColumLenght[mColumLenght.length - 1] - mColumLenght[i]);
            drawText(canvas, i * widthSpace + 2*leftPading, y, i);
        }

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    private float sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}