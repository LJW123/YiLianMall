package com.yilian.mall.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yilian.mall.R;

/**
 * 刮刮卡
 * 
 */
@SuppressLint("DrawAllocation")
public class GuaGuaKa extends View {

	private int lastX;
	private int lastY;
	private int height;
	private int width;
	private int movingX;
	private int movingY;
	private Bitmap mBitmap;
	private Paint mPaint;
	private Canvas mCanvas;
	private Path mPath;

	private Paint mTextPaint;
	private int mTextSize;
	private int[] mPixels;
	private volatile boolean isComlemete = false;
	private Message msg;
	Bitmap mOuterBitmap;
	private setOnGuaGuaComplemeteListener mGuaGuaComplemeteListener;

	public void setOnGuaGuaComplemeteListener(setOnGuaGuaComplemeteListener mGuaGuaComplemeteListener) {
		this.mGuaGuaComplemeteListener = mGuaGuaComplemeteListener;
	}

	public GuaGuaKa(Context context) {
		super(context);
		init(context);
	}

	public GuaGuaKa(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void again(Context context) {
		isComlemete = false;
		postInvalidate();
		init(context);
		getbitmap();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 1.1获取高度和宽度

		//height = MeasureSpec.getSize(heightMeasureSpec);

		//width = MeasureSpec.getSize(widthMeasureSpec);
		height = mOuterBitmap.getHeight();
		width = mOuterBitmap.getWidth();
		
		getbitmap();
	}

	public void getbitmap() {
		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		mCanvas = new Canvas(mBitmap);
		// 1.3设置绘制path画笔的属性
		setPath();

		// 2.1 设置textPaint属性
		setTextMessagePaint();
		mCanvas.drawBitmap(mOuterBitmap, 0, 0, null);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		movingX = (int) event.getX();
		movingY = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			touchMove(event);
			break;
		case MotionEvent.ACTION_UP:
			touchUp(event);
			break;

		default:
			break;
		}
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!isComlemete) {
			drawPath();
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
	}

	// 画path
	private void drawPath() {
		mPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_OUT));
		mCanvas.drawPath(mPath, mPaint);
	}

	// 手势抬起
	private void touchUp(MotionEvent event) {
		new Thread(mRunnable).start();
	}

	// 手势放下
	private void touchMove(MotionEvent event) {
		// int dx = Math.abs(movingX - lastX);
		// int dy = Math.abs(movingY - lastY);
		// if (dx > 1 || dy > 1) {
		// }
		mPath.lineTo(movingX, movingY);
		lastX = movingX;
		lastY = movingY;
	}

	// 手势按下
	private void touchDown(MotionEvent event) {
		lastX = movingX;
		lastY = movingY;
		mPath.moveTo(lastX, lastY);
	}

	/**
	 * 设置绘制path画笔的属性
	 */
	private void setPath() {
		mPaint.setColor(Color.parseColor("#c0c0c0"));
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(80);
	}

	/**
	 * 设置画刮刮卡画笔的属性
	 */
	private void setTextMessagePaint() {
		mTextPaint.setColor(Color.DKGRAY);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setStyle(Style.FILL);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		// ----1.遮盖层
		mPaint = new Paint();
		mPath = new Path();
		// ----2.遮盖层下的内容
		mTextPaint = new Paint();
		mOuterBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.scatchcard_gray)
				.copy(Bitmap.Config.ARGB_8888, true);

	}

	/**
	 * 开启线程计算用户擦除了多少区域
	 */
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			int w = getWidth();
			int h = getHeight();

			float wipeArea = 0;
			float totalArea = w * h;
			Bitmap bitmap = mBitmap;

			if (mPixels == null) {
				mPixels = new int[w * h];
			}
			// 获取bitmap上所有的像素信息
			bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int index = i + j * w;
					if (mPixels[index] == 0) {
						wipeArea++;
					}
				}
			}

			if (wipeArea > 0 && totalArea > 0) {
				int percent = (int) (wipeArea * 100 / totalArea);
				Log.e("System.out", "区域---" + percent);
				// 清楚图层区域
				if (percent > 30) {
					isComlemete = true;
					postInvalidate();
				}
				msg = mHandler.obtainMessage();
				msg.what = 0x1;
				msg.arg1 = percent;
				mHandler.sendMessage(msg);
			}
		}
	};
	// int count = 0;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mGuaGuaComplemeteListener != null) {
				int percent = msg.arg1;
				if (percent > 30) {
					mGuaGuaComplemeteListener.complemete();
					// count = 1;
				}
				// mGuaGuaComplemeteListener = null;

			}
		};
	};

	public interface setOnGuaGuaComplemeteListener {
		void complemete();
	}
}
