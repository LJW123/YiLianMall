package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.ActivityInfo;
import com.yilian.mall.entity.GetActivityInfoResult;
import com.yilian.mall.entity.GuessWinnerInfo;
import com.yilian.mall.entity.GuessingNumberEntity;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengDialog.OnListItemClickListener;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.DialogLayout;
import com.yilian.mall.utils.DialogLayout.OnBtnClickListener;
import com.yilian.mall.utils.DialogLayout.TextWatcher;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 猜数字详情界面
 */
public class GuessDetailActivity extends BaseActivity {

	DialogLayout dialogLayout;
	private TextView rightTextView;
	private TextView tv_back;
	private ScrollView guess_item_scroll;
	private TextView involved_tv, totalnumber_tv, residue_tv;
	private TextView attention_tv, giftname_tv, relation_tv;
	private TextView data_tv;
	private ProgressBar pro;
	private CircleImageView1 prize_iv;
	private BitmapUtils bitmapUtils;
	private ActivityNetRequest mActivityNetRequest;
	private String mActivityIndex;// 活动id
	private ActivityInfo activity;
	private String guessFigure;
	private String figure;
	private String count;
	private int integral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess_detail);
		ViewUtils.inject(this);
		mActivityNetRequest = new ActivityNetRequest(mContext);
		bitmapUtils = new BitmapUtils(mContext);
		mActivityIndex = getIntent().getStringExtra("activity_index");
		initView();
		if ("3".equals(getIntent().getStringExtra("activity_state"))) {
			getWinnerInfo();
		}
		getActicityInfo();
	}

	public void initView() {
		tv_back = (TextView) findViewById(R.id.tv_back);
		rightTextView = (TextView) findViewById(R.id.right_textview);
		guess_item_scroll = (ScrollView) findViewById(R.id.guess_item_scroll);
		guess_item_scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);// 禁止下拉
		guess_item_scroll.setFocusable(false);
		data_tv = (TextView) findViewById(R.id.data_tv);
		involved_tv = (TextView) findViewById(R.id.involved_tv);
		totalnumber_tv = (TextView) findViewById(R.id.totalnumber_tv);
		residue_tv = (TextView) findViewById(R.id.residue_tv);
		attention_tv = (TextView) findViewById(R.id.attention_tv);
		giftname_tv = (TextView) findViewById(R.id.giftname_tv);
		relation_tv = (TextView) findViewById(R.id.relation_tv);
		prize_iv = (CircleImageView1) findViewById(R.id.prize_iv);
		pro = (ProgressBar) findViewById(R.id.pro);

	}

	/**
	 * 联网获取猜数字详情界面的数据
	 */
	private void getActicityInfo() {

		mActivityNetRequest.getActivityInfo(mActivityIndex, new RequestCallBack<GetActivityInfoResult>() {

					@Override
					public void onStart() {
						super.onStart();
						guess_item_scroll.setVisibility(View.GONE);
						startMyDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<GetActivityInfoResult> arg0) {
						stopMyDialog();
						guess_item_scroll.setVisibility(View.VISIBLE);
						GetActivityInfoResult result = arg0.result;
						if (result == null || result.activity == null) {
							return;
						}
						switch (result.code) {
						case 1:
							activity = result.activity;

							tv_back.setText(activity.activityName);

							rightTextView.setText("我猜过的");

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String time = sdf.format(new Date(Long.valueOf(activity.startTime + "000"))).substring(0,
									10);
							data_tv.setText("发布时间 : " + time);

							involved_tv.setText(Html.fromHtml(
									"<font color='#ff6d0d'&nbsp;>" + activity.playCount + "</font>" + "<br/>" + "已参与"));

							totalnumber_tv.setText(Html.fromHtml(activity.totalCount + "<br/>" + "总需人次"));

							residue_tv.setText(Html.fromHtml("<font color='#57cdb1'>"
									+ (activity.totalCount - activity.playCount) + "</font>" + "<br/>" + "剩余"));

							attention_tv.setText(Html.fromHtml("1.中奖的用户请在 " + "<font color='#fe6682'>"
									+ activity.timeLimit / (24 * 60 * 60) + "</font>" + " 日内到指定的兑换中心领取奖品,过期作废!每次将消耗奖券"
									+ "<font color='#fe6682'>" + activity.expend + "乐分币" + "</font>" + "." + "<br/>"
									+ "2.猜的数字需要大于0小于10000，每人可猜测多次，数字不重复的前提下，猜的数字越多、越小，开奖时中奖机会越大." + "<br/>" + "3.开奖规则：系统自动开奖，开奖时从所有被猜的数字中找出最小且唯一的数字，猜测这个数字的人即为中奖者."
									+ "<br/>" + "4.若开奖时没有唯一的数字，则活动不会开奖，可继续参与猜数字."+"<br/>"+"5.本活动解释权归益联益家网所有."));

							giftname_tv.setText(activity.prizes.get(0).prizeName);
							bitmapUtils.display(prize_iv,
									Constants.ImageUrl + activity.prizes.get(0).prizeImgUrl + Constants.ImageSuffix);

							relation_tv.setText(Html.fromHtml(
									"1.本次活动负责人:" + activity.contacts + "<br/>" + "2.联系人电话:" + activity.phone + "<br/>"
											+ "3.兑换中心客服电话:" + activity.tel + "<br/>" + "4.兑换中心地址:" + activity.address));

							pro.setMax(activity.totalCount);
							pro.setProgress(activity.playCount);

							break;

						default:
							break;
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						stopMyDialog();
						guess_item_scroll.setVisibility(View.GONE);
					}
				});
	}

	private void getWinnerInfo() {
		mActivityNetRequest.getGuessWinner(mActivityIndex, new RequestCallBack<GuessWinnerInfo>() {

			@Override
			public void onSuccess(ResponseInfo<GuessWinnerInfo> arg0) {
				// TODO Auto-generated method stub
				GuessWinnerInfo winnerInfo=arg0.result;
				winnerDialog(winnerInfo);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 开始猜
	 */
	public void begin_guess(View view) {
		if (isLogin()) {
			dialogLayout = new DialogLayout(this, R.style.DialogControl, 0.8, -1);
			beginDialog();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager) GuessDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}, 500);
		} else {
			startActivity(new Intent(GuessDetailActivity.this, LeFenPhoneLoginActivity.class));
		}
	}

	public void beginDialog() {

		dialogLayout.changeItem();
		dialogLayout.setLebiText("（猜一次需要消耗" + activity.expend + "个乐分币）");
		dialogLayout.setEdit();
		dialogLayout.setCloseListner(new OnBtnClickListener() {

			@Override
			public void onBtnClicked() {
				dialogLayout.dismiss();
			}
		});

		dialogLayout.setgetlebiListner(new OnBtnClickListener() {

			@Override
			public void onBtnClicked() {
				Intent intent = new Intent(GuessDetailActivity.this, WebViewActivity.class);
				intent.putExtra("url", Constants.ObtainLefen);
				intent.putExtra("title_name", getString(R.string.how_to_obtain_lefen));
				startActivity(intent);
				dialogLayout.dismiss();
			}
		});
		dialogLayout.setLongListner(new OnBtnClickListener() {

			@Override
			public void onBtnClicked() {

				guessFigure = dialogLayout.getEdit();// 获取输入的数字
				dialogLayout.dismiss();
				getGuessData();
			}
		});
		dialogLayout.seteditListner(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s.toString())) {
					dialogLayout.changeBac();
				} else {
					dialogLayout.noChangeBac();
				}
			}
		});
		if (!dialogLayout.isShowing()) {
			dialogLayout.show();
		}

	}

	/**
	 * 联网获取猜数字弹框的数据
	 */
	public void getGuessData() {
		startMyDialog();
		mActivityNetRequest.getGuess(mActivityIndex, guessFigure, new RequestCallBack<GuessingNumberEntity>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				stopMyDialog();
				showToast(R.string.net_work_not_available);
			}

			@Override
			public void onSuccess(ResponseInfo<GuessingNumberEntity> arg0) {
				stopMyDialog();
				switch (arg0.result.code) {
				case 1:
					againDialog();
					figure = arg0.result.guess_figure;
					count = arg0.result.guess_count;
					integral = arg0.result.integral;
					getActicityInfo();

					break;
				case -13:
					if (integral < activity.expend) {
                        showToast("奖励不足");
                    }
                    break;
				case -22:
					if (activity.totalCount == activity.playCount || activity.totalCount < activity.playCount) {
						showToast("活动已结束");
					}
					break;
				default:
					break;
				}

			}
		});
	}

	/**
	 * 猜过数字之后的弹框
	 */
	public void againDialog() {

		dialogLayout.againItem();
		dialogLayout.getdialog_msgs("您在本次 “" + activity.activityName + "” 活动中猜的数字是 “" + guessFigure + "” ,再有“"
				+ (activity.totalCount - activity.playCount - 1) + "”人参与就可公布获奖结果，请耐心等待！");
		dialogLayout.setgetShareListner(new OnBtnClickListener() {

			@Override
			public void onBtnClicked() {
				Animation animBottom = AnimationUtils.loadAnimation(GuessDetailActivity.this,
						R.anim.anim_wellcome_bottom);
				UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
						new UmengGloble().getAllIconModels());
				dialog1.setItemLister(new OnListItemClickListener() {

					@Override
					public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
						String content = "我在参与大家猜活动,你也赶快来试试吧…好运天天有，精美好礼，等你来拿";
						new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null,
								Ip.SHARE_URL).share();

					}
				});

				dialog1.show();
			}
		});
		dialogLayout.setgetagainsListner(new OnBtnClickListener() {

			@Override
			public void onBtnClicked() {
				beginDialog();
				// dialogLayout.dismiss();
			}
		});

		if (!dialogLayout.isShowing()) {
			dialogLayout.show();
		}

	}
	
	public void winnerDialog(GuessWinnerInfo winnerInfo) {
		String time="中奖日期  "+new SimpleDateFormat("yyyy/MM/dd",Locale.US).format(new Date(winnerInfo.prizeTime*1000));
		String user="中奖用户  "+winnerInfo.userPhone.replace(winnerInfo.userPhone.substring(3, 7), "****");
		
		final AlertDialog dialog=new AlertDialog.Builder(mContext,R.style.DialogControl).create();
		
		View view=getLayoutInflater().inflate(R.layout.guess_winner_dialog_layout, null);
		TextView tv_msg=(TextView) view.findViewById(R.id.dialog_msg);
		TextView tv_name=(TextView) view.findViewById(R.id.tv_prize_name);
		TextView tv_tel=(TextView) view.findViewById(R.id.tv_user_tel);
		TextView tv_time=(TextView) view.findViewById(R.id.tv_prize_time);
		ImageButton btn_know=(ImageButton)view.findViewById(R.id.dialog_button);
		tv_msg.setText("该活动中奖数字  \""+winnerInfo.figure+"\"");
		tv_name.setText(winnerInfo.prizeName);
		tv_tel.setText(user);
		tv_time.setText(time);
		btn_know.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
		ImageLoader.getInstance().displayImage(Constants.ImageUrl+winnerInfo.prizeImgUrl, (ImageView) view.findViewById(R.id.imgView_icon));
		dialog.setCancelable(false);
		dialog.show();
		dialog.setContentView(view);
		WindowManager.LayoutParams lp= dialog.getWindow().getAttributes();
		lp.y=20;
		lp.width=(int) (CommonUtils.getHeight(this, "w")*0.9);
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 我猜过的
	 */
	public void rightTextview(View v) {
		if (isLogin()) {
			Intent intent = new Intent(this, GuessedListActivity.class);
			intent.putExtra("activity_index", mActivityIndex);
			intent.putExtra("activity_name", activity.activityName);
			startActivity(intent);
		} else {
			startActivity(new Intent(GuessDetailActivity.this, LeFenPhoneLoginActivity.class));
		}
	}

}
