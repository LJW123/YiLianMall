package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.PrizeListAdapter;
import com.yilian.mall.entity.ActivityInfo;
import com.yilian.mall.entity.GetActivityInfoResult;
import com.yilian.mall.entity.ScrapeEntity;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengDialog.OnListItemClickListener;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.GuaGuaKa;
import com.yilian.mall.utils.GuaGuaKa.setOnGuaGuaComplemeteListener;
import com.yilian.mall.utils.ScrollViewExtend;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Ip;

/**
 * 刮刮卡详情界面
 */
public class GuaGuaKaActivity extends BaseActivity {

	@ViewInject(R.id.guaguaka_tuceng)
	GuaGuaKa guaguaka_tuceng;
	/**
	 * 参加刮刮乐活动 刮开涂层获取数据
	 */
	String name;
	int voucher;
	int type;
	private String mActivityIndex;// 活动id
	private ActivityNetRequest mActivityNetRequest;
	private ActivityInfo activity;
	@ViewInject(R.id.getwinning_ll)
	private LinearLayout getwinning_ll;// 获奖后显示的页面
	@ViewInject(R.id.defeated_ll)
	private LinearLayout defeated_ll;// 未获奖显示的界面
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.guaguaka_item_scroll)
	private ScrollViewExtend guaguaka_item_scroll;
	@ViewInject(R.id.expend_tv)
	private TextView expend_tv;
	@ViewInject(R.id.guagua_bac_iv)
	private LinearLayout guagua_bac_iv;
	@ViewInject(R.id.guaguaka_list_prize_type)
	private NoScrollListView guaguaka_list_prize_type;
	@ViewInject(R.id.prize_name)
	private TextView prize_name;
	@ViewInject(R.id.guaguaka_zy_tv)
	private TextView guaguaka_zy_tv;
	@ViewInject(R.id.guaguaka_infor_tv)
	private TextView guaguaka_infor_tv;
	@ViewInject(R.id.font)
	private ImageView font;
	@ViewInject(R.id.sure_tv)
	private TextView sure_tv;
    @ViewInject(R.id.guaguaka_share_iv)
    private ImageView guaguaka_share_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guaguaka);
		ViewUtils.inject(this);
		mActivityIndex = getIntent().getStringExtra("activity_index");

		Logger.i(mActivityIndex+"SSSSSSSSSSSSSSSSSS");
		mActivityNetRequest = new ActivityNetRequest(mContext);

//		tv_back.setText(getIntent().getStringExtra("activity_name"));
		guaguaka_item_scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);// 禁止下拉
		guaguaka_list_prize_type.setFocusable(false);
		guaguaka_list_prize_type.setEnabled(false);

		getActicityInfo();

	}

	/**
	 * 联网获取刮刮乐游戏详情界面的数据
	 */
	private void getActicityInfo() {
		mActivityNetRequest.getActivityInfo( mActivityIndex, new RequestCallBack<GetActivityInfoResult>() {

					@Override
					public void onStart() {

						super.onStart();
						startMyDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<GetActivityInfoResult> arg0) {
						stopMyDialog();

						GetActivityInfoResult result = arg0.result;
						if (result == null || result.activity == null) {
							return;
						}
						switch (arg0.result.code) {
						case 1:
							activity = result.activity;
							tv_back.setText(activity.activityName);
							expend_tv.setText("每刮一次需要消耗" + activity.expend + "乐分币");

							PrizeListAdapter adapter = new PrizeListAdapter(GuaGuaKaActivity.this, result.activity);
							guaguaka_list_prize_type.setAdapter(adapter);

							guaguaka_zy_tv.setText(Html.fromHtml("1.仅限在" + activity.timeLimit / (24 * 60 * 60)
									+ "日内到指定的兑换中心领取,过期作废!每次抽奖消耗奖券" + "<font color='#fff497'>" + activity.expend + "乐分币"
									+ "</font>" + "<br/>" + "2.奖励根据抽奖次数范围,阶梯变化,抽奖次数越多,中大奖的几率越高." + "<br/>"
									+ "3.活动禁止作弊,一经发现,扣除所有奖励所得!" + "<br/>" + "4.本次活动解释权归益联益家网所有."));

							guaguaka_infor_tv.setText(Html.fromHtml(
									"本次活动负责人:" + activity.contacts + "<br/>" + "联系人电话:" + activity.phone + "<br/>"
											+ "兑换中心客服电话:" + activity.tel + "<br/>" + "兑换中心地址:" + activity.address));
							break;

						default:
							break;
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						stopMyDialog();
						guaguaka_item_scroll.setVisibility(View.GONE);

					}
				});
	}

	private void getScrapeData() {
		startMyDialog();
		mActivityNetRequest.getScrapeActivityResult(mActivityIndex, new RequestCallBack<ScrapeEntity>() {

			@Override
			public void onSuccess(ResponseInfo<ScrapeEntity> arg0) {
				stopMyDialog();
				switch (arg0.result.code) {
				case 1:
					name = arg0.result.prize_name;
					voucher = arg0.result.prize_voucher;
					type = arg0.result.prize_type;
					font.setVisibility(View.GONE);
					sure_tv.setVisibility(View.GONE);
					guaguaka_tuceng.setVisibility(View.VISIBLE);
					guagua_bac_iv.setBackgroundResource(R.mipmap.scratchcard_yellowbg);
					guagua_bac_iv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
							View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
					guaguaka_tuceng.getLayoutParams().height = guagua_bac_iv.getMeasuredHeight();
					guaguaka_tuceng.getLayoutParams().width = guagua_bac_iv.getMeasuredWidth();
					guaguaka_tuceng.setOnGuaGuaComplemeteListener(new setOnGuaGuaComplemeteListener() {

						@Override
						public void complemete() {
							if (voucher == 0) {
								guaguaka_tuceng.setVisibility(View.GONE);
								defeated_ll.setVisibility(View.VISIBLE);
								getwinning_ll.setVisibility(View.GONE);
								guagua_bac_iv.setBackgroundResource(R.mipmap.bg_hei);
								guaguaka_share_iv.setVisibility(View.GONE);
							} else {
								guaguaka_tuceng.setVisibility(View.GONE);
								guagua_bac_iv.setBackgroundResource(R.mipmap.scratchcard_yellowbg);
								defeated_ll.setVisibility(View.GONE);
								getwinning_ll.setVisibility(View.VISIBLE);
								prize_name.setText("获得了" + name);
								guaguaka_share_iv.setVisibility(View.VISIBLE);
							}

						}
					});

					break;
				case -13:
					if (arg0.result.integral < activity.expend) {
                        showToast("奖励不足");
                    }
                    break;
				case -22:
					showToast("活动已结束");
					break;
				case -4:
					showToast("登录状态失效,请重新登录");
					startActivity(new Intent(GuaGuaKaActivity.this, LeFenPhoneLoginActivity.class));
					break;
				default:
					break;
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				stopMyDialog();
			}
		});
	}

	/**
	 * 分享
	 * @param v
	 */
	public void shares(View v) {
		/*Intent intent = new Intent(mContext, AgreementActivity.class);
		intent.putExtra("title", "活动规则");
		intent.putExtra("url", Constants.ActivityRules);
		startActivity(intent);*/
	Animation	animBottom = AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom);
		UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
				new UmengGloble().getAllIconModels());
		dialog1.setItemLister(new OnListItemClickListener() {

			@Override
			public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
				String content = "我在益联益家刮刮卡活动中抽中了" + name +",你也赶快来试试吧…好运天天有，精美好礼，等你来拿";
				new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, Ip.SHARE_URL)
						.share();

			}
		});

		dialog1.show();
	}

	/**
	 * 立即兑换
	 */
	public void convert(View view) {
		if (type == 0) {
            Intent intent = new Intent(mContext, V3MoneyActivity.class);
            intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
            startActivity(intent);
		} else {
			Intent intent = new Intent(mContext, PrizeVoucherListActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 再来一次
	 */
	public void again(View view) {
		guaguaka_share_iv.setVisibility(View.GONE);
		getwinning_ll.setVisibility(View.GONE);
		defeated_ll.setVisibility(View.GONE);
		guaguaka_tuceng.again(mContext);
		getScrapeData();
	}

	/**
	 * 没有中奖再来一次
	 */
	public void againone(View view) {
		guaguaka_share_iv.setVisibility(View.GONE);
		getwinning_ll.setVisibility(View.GONE);
		defeated_ll.setVisibility(View.GONE);
		guaguaka_tuceng.again(mContext);
		getScrapeData();
	}

	public void sure(View view) {
		// 玩游戏前判断是否已经登录
		if (isLogin()) {
			getScrapeData();
		} else {
			startActivity(new Intent(GuaGuaKaActivity.this, LeFenPhoneLoginActivity.class));
		}

	}

}
