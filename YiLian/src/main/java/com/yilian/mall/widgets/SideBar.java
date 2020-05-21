package com.yilian.mall.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yilian.mall.R;

public class SideBar extends View {

//	public static String[] b = { "A", "B", "C", "F", "G", "H", "J", "L", "N", "Q", "S", "T", "X", "Y", "Z", "#" };
	public static String[] b = {"A", "B", "C", "D","E","F", "G", "H", "I",
			"J", "K","L","M", "N","O","P", "Q","R", "S", "T","U","V","W","X", "Y", "Z"};
	private Paint paint = new Paint();
	private int choose = -1;
	private TextView mDialog;
	private setOnTouchLetterChangedListener listener;

	public setOnTouchLetterChangedListener getListener() {
		return listener;
	}

	public void setListener(setOnTouchLetterChangedListener listener) {
		this.listener = listener;
	}

	public TextView getmDialog() {
		return mDialog;
	}

	public void setmDialog(TextView mDialog) {
		this.mDialog = mDialog;
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();// 获取高
		int width = getWidth();// 获取控件宽
		int singleHeight = height / b.length;// 每一个字母的高度

		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.parseColor("#10173e"));// 文字的颜色
			//paint.setTypeface(Typeface.DEFAULT_BOLD);// 粗体
			paint.setAntiAlias(true);
			paint.setTextSize(dip2px(14));
			float xPos = (width - paint.measureText(b[i])) / 2;
			float yPos = (singleHeight) * (i + 1);
			canvas.drawText(b[i], xPos, yPos, paint);// 绘制文本
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		float y = event.getY();// 获取手指在控件的y坐标
		int oldChoose = choose;
		int c = (int) ((y / getHeight()) * b.length);// 被选中字母的下标
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
			choose = -1;
			invalidate();
//			if (mDialog != null) {
//				mDialog.setVisibility(View.INVISIBLE);// 隐藏那个中间的绿色矩形
//			}
			break;

		default:
			// 按下去或者move的状态
			setBackgroundResource(R.drawable.sidebar_background);// 设置红色背景
			if (c != oldChoose && c <= b.length - 1) {
				// 选中了一个新的字母
				if (listener != null) {
					try {
						listener.onTouchLetterChanged(b[c]);
					} catch (Exception e) {
						System.out.print("我就越界,我就不崩,气死你,啦啦啦o(^▽^)o");
					}
				}
//				if (mDialog != null) {
//					mDialog.setText(b[c]);// 设置中间矩形的文本为选中的字母
//					mDialog.setVisibility(View.VISIBLE);// 设置可见
//				}
			}
			choose = c;
			invalidate();// 重绘
			break;
		}
		return true;
	}

	/**
	 * 将dip变成px
	 * 
	 * @param dip
	 * @return
	 */
	public int dip2px(int dip) {
		float scale = this.getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	public interface setOnTouchLetterChangedListener {
		public void onTouchLetterChanged(String s);
	}
}
