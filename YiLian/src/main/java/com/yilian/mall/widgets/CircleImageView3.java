package com.yilian.mall.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yilian.mall.R;


public class CircleImageView3 extends ImageView {

	private float myRadious;

	public CircleImageView3(Context context) {
		super(context);
	}
	public CircleImageView3(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public CircleImageView3(Context context, AttributeSet attrs,
                            int defStyle) {
		super(context, attrs, defStyle);
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImageView3, defStyle, 0);
		int indexCount = typedArray.getIndexCount();
		for (int i = 0; i < indexCount; i++) {
			int index = typedArray.getIndex(i);
			switch (index){
				case R.styleable.CircleImageView3_radious:
					myRadious = typedArray.getFloat(index,0);
					break;

			}
		}
		typedArray.recycle();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addRoundRect(new RectF(0, 0, w, h), myRadious, myRadious, Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}
}