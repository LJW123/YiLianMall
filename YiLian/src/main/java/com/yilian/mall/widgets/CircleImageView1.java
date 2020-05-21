package com.yilian.mall.widgets;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;


public class CircleImageView1 extends ImageView {
	public CircleImageView1(Context context) {
		super(context);
	}
	public CircleImageView1(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public CircleImageView1(Context context, AttributeSet attrs,
                            int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addRoundRect(new RectF(0, 0, w, h), 6.0f, 6.0f, Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}
}