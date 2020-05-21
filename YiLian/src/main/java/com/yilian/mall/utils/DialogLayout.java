package com.yilian.mall.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.EventListener;

public class DialogLayout extends Dialog implements android.view.View.OnClickListener, TextWatcher {

	private Context mContext;
	private View view;

	private EditText dialog_edit;
	private ImageView getlebi, close;
	private Button dialog_button_long;
	private OnBtnClickListener longListner;
	private OnBtnClickListener closeListner;
	private OnBtnClickListener getlebiListner;
	private OnBtnClickListener getShareListner;
	private OnBtnClickListener getagainsListner;

	private TextWatcher editListner;
	private ImageView hengxian, hengxiantwo;
	private TextView dialog_msg, lebi;
	private TextView dialog_msgs;
	private ImageView holdon;
	private LinearLayout again_share_ll;
	private ImageButton imgBtn_get_again, imgBtn_get_shares;
	private TextView dialog_title;

	public DialogLayout(Context context, int theme, double width, double height) {
		super(context, theme);
		mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.control_dialog, null);
		setContentView(view);
		WindowManager m = getWindow().getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = getWindow().getAttributes();
		getWindow().setGravity(Gravity.TOP);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		p.y = 220;
		p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.65
		if (height > 0) {
			p.height = (int) (d.getHeight() * height);
		}
		getWindow().setAttributes(p);
		dialog_msgs = (TextView) view.findViewById(R.id.dialog_msgs);
		holdon = (ImageView) view.findViewById(R.id.holdon);
		close = (ImageView) view.findViewById(R.id.close);
		getlebi = (ImageView) view.findViewById(R.id.getlebi);
		dialog_edit = (EditText) view.findViewById(R.id.dialog_edit);
		dialog_button_long = (Button) view.findViewById(R.id.dialog_button_long);
		hengxian = (ImageView) view.findViewById(R.id.hengxian);
		hengxiantwo = (ImageView) view.findViewById(R.id.hengxiantwo);
		dialog_msg = (TextView) view.findViewById(R.id.dialog_msg);
		lebi = (TextView) view.findViewById(R.id.lebi);
		again_share_ll = (LinearLayout) view.findViewById(R.id.again_share_ll);
		imgBtn_get_again = (ImageButton) view.findViewById(R.id.imgBtn_get_again);
		imgBtn_get_shares = (ImageButton) view.findViewById(R.id.imgBtn_get_shares);
		dialog_title=(TextView) view.findViewById(R.id.dialog_title);
		close.setOnClickListener(this);
		dialog_button_long.setOnClickListener(this);
		dialog_edit.addTextChangedListener(this);
		getlebi.setOnClickListener(this);
		dialog_button_long.setClickable(false);
		imgBtn_get_again.setOnClickListener(this);
		imgBtn_get_shares.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public String getEdit()

	{
		return dialog_edit.getText().toString();

	}

	public void setEdit() {
		dialog_edit.setText("");
	}

	public void setLebiText(String lefen) {
		lebi.setText(lefen);
	}

	public void setEditVisible(boolean isVisible) {
		dialog_edit.setVisibility(View.VISIBLE);
	}

	public String getEditText() {
		if (dialog_edit.getText() == null) {
			return "";
		} else {
			return dialog_edit.getText().toString();
		}
	}

	public void changeBac() {
		dialog_button_long.setBackgroundResource(R.drawable.guess_sure_btn_yellow);
		dialog_button_long.setClickable(true);
		dialog_edit.setBackgroundResource(R.mipmap.tanchuang_kuang);
	}

	public void noChangeBac() {
		dialog_button_long.setBackgroundResource(R.drawable.guess_sure_btn_gray);
		dialog_button_long.setClickable(false);
		dialog_edit.setBackgroundResource(R.mipmap.tanchuang_gray);

	}

	public interface OnBtnClickListener extends EventListener {
		public void onBtnClicked();
	}

	public interface TextWatcher extends EventListener {
		public void beforeTextChanged(CharSequence s, int start, int count, int after);

		public void onTextChanged(CharSequence s, int start, int before, int count);

		public void afterTextChanged(Editable s);
	}

	public void setLongListner(OnBtnClickListener longListner) {
		this.longListner = longListner;
	}

	public void setCloseListner(OnBtnClickListener closeListner) {
		this.closeListner = closeListner;
	}

	public void setgetShareListner(OnBtnClickListener getShareListner) {
		this.getShareListner = getShareListner;

	}

	public void setgetlebiListner(OnBtnClickListener getlebiListner) {
		this.getlebiListner = getlebiListner;

	}

	public void setgetagainsListner(OnBtnClickListener getagainsListner) {
		this.getagainsListner = getagainsListner;

	}

	public void seteditListner(TextWatcher editListner) {
		this.editListner = editListner;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			if (closeListner != null) {
				closeListner.onBtnClicked();
			}
			dismiss();
			break;
		case R.id.imgBtn_get_again:
			if (getagainsListner != null) {
				getagainsListner.onBtnClicked();
			}
			break;
		case R.id.imgBtn_get_shares:
			if (getShareListner != null) {
				getShareListner.onBtnClicked();
			}
			break;
		case R.id.getlebi:
			if (getlebiListner != null) {
				getlebiListner.onBtnClicked();
			}
			break;
		case R.id.dialog_button_long:
			if (longListner != null) {
				longListner.onBtnClicked();
			}
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (editListner != null) {
			editListner.beforeTextChanged(s, start, count, after);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (editListner != null) {
			editListner.onTextChanged(s, start, before, count);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (editListner != null) {
			editListner.afterTextChanged(s);
		}
	}

	public void changeItem() {
		dialog_msgs.setVisibility(View.GONE);
		holdon.setVisibility(View.GONE);
		again_share_ll.setVisibility(View.GONE);

		dialog_button_long.setVisibility(View.VISIBLE);
		hengxian.setVisibility(View.VISIBLE);
		hengxiantwo.setVisibility(View.VISIBLE);
		dialog_msg.setVisibility(View.VISIBLE);
		lebi.setVisibility(View.VISIBLE);
		dialog_edit.setVisibility(View.VISIBLE);
		getlebi.setVisibility(View.VISIBLE);
	}

	public void againItem() {
		dialog_msgs.setVisibility(View.VISIBLE);
		holdon.setVisibility(View.VISIBLE);
		again_share_ll.setVisibility(View.VISIBLE);

		dialog_button_long.setVisibility(View.GONE);
		hengxian.setVisibility(View.GONE);
		hengxiantwo.setVisibility(View.GONE);
		dialog_msg.setVisibility(View.GONE);
		lebi.setVisibility(View.GONE);
		dialog_edit.setVisibility(View.GONE);
		getlebi.setVisibility(View.GONE);
	}
	
	
	public void setWinnerInfo(String prize_name,String user_phone,String prize_time,String img_url) {
		((TextView) view.findViewById(R.id.tv_prize_name)).setText(prize_time);
		((TextView) view.findViewById(R.id.tv_user_tel)).setText(user_phone);
		((TextView) view.findViewById(R.id.tv_prize_time)).setText(prize_time);
		ImageLoader.getInstance().displayImage(img_url, (ImageView) view.findViewById(R.id.imgView_icon));
	}

	public void getdialog_msgs(String value) {
		dialog_msgs.setText(value);

	}
	
	public void setdialog_msg(String msg) {
		dialog_msg.setText(msg);
	}
	
	public void setdialog_title(String msg) {
		dialog_title.setText(msg);
	}
	
	
}
