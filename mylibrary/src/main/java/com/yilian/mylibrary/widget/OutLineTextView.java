package com.yilian.mylibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilian.mylibrary.R;

/**
 * Created by  on 2017/9/22 0022.
 */

public class OutLineTextView extends TextView {

    private TextView borderText = null;///用于描边的TextView

    public TextView getOutLineTextView() {
        return borderText;
    }

    public OutLineTextView(Context context) {
        super(context);
        borderText = new TextView(context);
        init();
    }

    public OutLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context, attrs);
        init();
    }

    public OutLineTextView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        borderText = new TextView(context, attrs, defStyle);
        init();
    }

    public void init() {
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(4);                                  //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
        borderText.setTextColor(Color.parseColor("#fff7f8"));  //设置描边颜色
        borderText.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        //两个TextView上的文字必须一致
        if (tt == null || !tt.equals(this.getText())) {
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }

}
