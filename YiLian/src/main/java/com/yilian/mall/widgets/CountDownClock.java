package com.yilian.mall.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.utils.CommonUtils;

/**
 * 倒计时控件
 * 
 * @author Administrator
 *
 */
public class CountDownClock extends LinearLayout implements Runnable {

	private static final int RED = 0Xffec3f3f;
	private static final int WHITE = 0Xffffffff;

	private TextView mDayTxt;
	private TextView mHourTxt1;
	private TextView mHourTxt2;
	private TextView mMinuteTxt1;
	private TextView mMinuteTxt2;
	private TextView mSecondTxt1;
	private TextView mSecondTxt2;

	private TextView mTxtSem1;
	private TextView mTxtSem2;

	private long mTotalSeconds;
	private CountDownListener mListener;

	public CountDownClock(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context, null);
	}

	public CountDownClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	public CountDownClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		// setOrientation(HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = CommonUtils.dip2px(context, 2);
		mDayTxt = new TextView(context);
		mHourTxt1 = new TextView(context);
		mHourTxt2 = new TextView(context);
		mMinuteTxt1 = new TextView(context);
		mMinuteTxt2 = new TextView(context);
		mSecondTxt1 = new TextView(context);
		mSecondTxt2 = new TextView(context);
		mTxtSem1 = new TextView(context);
		mTxtSem2 = new TextView(context);

		mTxtSem1.getPaint().setFakeBoldText(true);
		mTxtSem2.getPaint().setFakeBoldText(true);
		mTxtSem1.setText(":");
		mTxtSem2.setText(":");

		mDayTxt.setTextColor(RED);
		mDayTxt.setTextSize(12);
		mDayTxt.setPadding(0, 0, CommonUtils.dip2px(context, 5), 0);
		mHourTxt1.setTextColor(WHITE);
		mHourTxt2.setTextColor(WHITE);
		mMinuteTxt1.setTextColor(WHITE);
		mMinuteTxt2.setTextColor(WHITE);
		mSecondTxt1.setTextColor(WHITE);
		mSecondTxt2.setTextColor(WHITE);

		int txtPadding = CommonUtils.dip2px(context, 2);
		mHourTxt1.setPadding(txtPadding, 0, txtPadding, 0);
		mHourTxt2.setPadding(txtPadding, 0, txtPadding, 0);
		mMinuteTxt1.setPadding(txtPadding, 0, txtPadding, 0);
		mMinuteTxt2.setPadding(txtPadding, 0, txtPadding, 0);
		mSecondTxt1.setPadding(txtPadding, 0, txtPadding, 0);
		mSecondTxt2.setPadding(txtPadding, 0, txtPadding, 0);
		
		mHourTxt1.setBackgroundResource(R.drawable.countdown_txt_bg);
		mHourTxt2.setBackgroundResource(R.drawable.countdown_txt_bg);
		mMinuteTxt1.setBackgroundResource(R.drawable.countdown_txt_bg);
		mMinuteTxt2.setBackgroundResource(R.drawable.countdown_txt_bg);
		mSecondTxt1.setBackgroundResource(R.drawable.countdown_txt_bg);
		mSecondTxt2.setBackgroundResource(R.drawable.countdown_txt_bg);
		// mHourTxt1.setBackgroundColor(BLACK);
		// mHourTxt2.setBackgroundColor(BLACK);
		// mMinuteTxt1.setBackgroundColor(BLACK);
		// mMinuteTxt2.setBackgroundColor(BLACK);
		// mSecondTxt1.setBackgroundColor(BLACK);
		// mSecondTxt2.setBackgroundColor(BLACK);

		addView(mDayTxt, lp);
		addView(mHourTxt1, lp);
		addView(mHourTxt2, lp);
		addView(mTxtSem1, lp);
		addView(mMinuteTxt1, lp);
		addView(mMinuteTxt2, lp);
		addView(mTxtSem2, lp);
		addView(mSecondTxt1, lp);
		addView(mSecondTxt2, lp);
	}

	long mday;
	long mhour;
	private long mmin;// 天，小时，分钟，秒
	long msecond;
	private boolean run = false; // 是否启动了

	public void setTimes(long seconds) {
		mTotalSeconds=seconds;
		msecond = seconds % 60;
		mday = seconds / (24 * 60 * 60);
		mhour = (seconds / (60 * 60)) % 24;
		mmin = (seconds / 60) % 60;

	}

	/**
	 * 倒计时计算
	 */
	private void ComputeTime() {
		mTotalSeconds--;
		msecond--;
		if (msecond < 0) {
			mmin--;
			msecond = 59;
			if (mmin < 0) {
				mmin = 59;
				mhour--;
				if (mhour < 0) {
					// 倒计时结束，一天有24个小时
					mhour = 23;
					mday--;

				}
			}

		}

	}

	public boolean isRun() {
		return run;
	}

	public void beginRun() {
		this.run = true;
		run();
	}

	public void stopRun() {
		this.run = false;
	}
	
	public void setCountDownListener(CountDownListener listener) {
		mListener=listener;
	}

	@Override
	public void run() {
		// 标示已经启动
		if (run) {
			ComputeTime();

			mDayTxt.setText(mday + "天");
			if (mhour < 10) {
				mHourTxt1.setText("0");
				mHourTxt2.setText(mhour + "");
			} else {
				mHourTxt1.setText(mhour / 10 + "");
				mHourTxt2.setText(mhour % 10 + "");
			}

			if (mmin < 10) {
				mMinuteTxt1.setText("0");
				mMinuteTxt2.setText(mmin + "");
			} else {
				mMinuteTxt1.setText(mmin / 10 + "");
				mMinuteTxt2.setText(mmin % 10 + "");
			}

			if (msecond < 10) {
				mSecondTxt1.setText("0");
				mSecondTxt2.setText(msecond + "");
			} else {
				mSecondTxt1.setText(msecond / 10 + "");
				mSecondTxt2.setText(msecond % 10 + "");
			}
			
			if (mTotalSeconds<=0) {
				stopRun();
				if (mListener!=null) {
					mListener.onStop();
				}
			}

			postDelayed(this, 1000);
		} else {
			removeCallbacks(this);
		}
	}

	public interface CountDownListener {

		/**
		 * 倒计时结束
		 */
		public abstract void onStop();
	}
}
