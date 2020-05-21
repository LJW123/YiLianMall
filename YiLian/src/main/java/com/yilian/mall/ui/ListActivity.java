package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshBase.Mode;
import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.ActivityList;
import com.yilian.mall.entity.MainActivityList;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动list界面
 */
public class ListActivity extends BaseActivity {

	private int pageIndex = 0;

	@ViewInject(R.id.refreshable_view)
	private PullToRefreshScrollView refreshable_view;

	@ViewInject(R.id.tv_back)
	private TextView tvBack;

	ArrayList<MainActivityList> mainActivitys;

	@ViewInject(R.id.lv_activity)
	private ListView lvActivity;

	@ViewInject(R.id.right_textview)
	private TextView rightTextView;

	@ViewInject(R.id.iv_no_date)
	ImageView mImgNoData;

	private ActivityNetRequest mActivityRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list);
		ViewUtils.inject(this);
		final String activityType = this.getIntent().getStringExtra("activityType");
		tvBack.setText(this.getIntent().getStringExtra("activityName"));

		setActivityList(activityType);

		if (activityType.equals("5")) {
			rightTextView.setText("");
		} else {
			rightTextView.setText("活动规则");
			rightTextView.setTextSize(14);
			rightTextView.setTextColor(getResources().getColor(R.color.color_red));
		}
		if (this.getIntent().getStringExtra("activityName").equals("消息")) {
			lvActivity.setVisibility(View.GONE);
			mImgNoData.setVisibility(View.VISIBLE);
			mImgNoData.setImageDrawable(getResources().getDrawable(R.mipmap.no_info));
		}
		mainActivitys = new ArrayList<MainActivityList>();
		// refreshable_view.setOnRefreshListener(new PullToRefreshListener() {
		// @Override
		// public void onRefresh() {
		// runOnUiThread(new Runnable() {
		// public void run() {
		// startMyDialog();
		// }
		// });
		// setActivityList(activityType);
		// }
		// }, 0);

		refreshable_view.setMode(Mode.PULL_FROM_START);
		refreshable_view.getLoadingLayoutProxy(true, false)
				.setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));
		refreshable_view.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				setActivityList(activityType);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
//				setActivityList(activityType);
			}
		});

		lvActivity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				MainActivityList activity = (MainActivityList) parent.getItemAtPosition(position);

				Intent i;
				if (activity.getActivity_type().equals(Constants.ACTIVITY_TYPE_SHAKE)) {
					if ("2".equals(activity.getActivity_status())) {//摇一摇2表示结束
						i = new Intent(ListActivity.this, PrizeWinnerListActivity.class);
						i.putExtra("activity_type", Constants.ACTIVITY_TYPE_SHAKE);
					} else {
						i = new Intent(ListActivity.this, YaoYiYaoActivity.class);
					}
					i.putExtra("activity_index", activity.getActivity_index());
					i.putExtra("activity_name", activity.getActivity_name());
					i.putExtra("activity_city", activity.getActivity_city());
					startActivity(i);
				}
				if (activity.getActivity_type().equals(Constants.ACTIVITY_TYPE_TURN)) {
					if ("3".equals(activity.getActivity_status())) {
						i = new Intent(ListActivity.this, PrizeWinnerListActivity.class);
						i.putExtra("activity_type", Constants.ACTIVITY_TYPE_TURN);
					} else {
						i = new Intent(ListActivity.this, DaZhuanPanDetailActivity.class);
					}
					i.putExtra("activity_index", activity.getActivity_index());
					i.putExtra("activity_name", activity.getActivity_name());
					startActivity(i);
				}
				if (activity.getActivity_type().equals(Constants.ACTIVITY_TYPE_SCRAPING)) {
					if ("3".equals(activity.getActivity_status())) {
						i = new Intent(ListActivity.this, PrizeWinnerListActivity.class);
						i.putExtra("activity_type", Constants.ACTIVITY_TYPE_SCRAPING);
					} else {
						i = new Intent(ListActivity.this, GuaGuaKaActivity.class);
					}
					i.putExtra("activity_index", activity.getActivity_index());
					i.putExtra("activity_name", activity.getActivity_name());
					startActivity(i);
				}
				if (activity.getActivity_type().equals(Constants.ACTIVITY_TYPE_GUESS)) {
                    showToast("活动已结束");
//					i = new Intent(ListActivity.this, GuessDetailActivity.class);
//					if ("3".equals(activity.getActivity_status())) {
//						i.putExtra("activity_state", activity.getActivity_status());
//					}
//					i.putExtra("activity_index", activity.getActivity_index());
//					i.putExtra("activity_name", activity.getActivity_name());
//					startActivity(i);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	public void rightTextview(View v) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra("title_name", getString(R.string.activity_rule));
		intent.putExtra("url", Constants.ActivityRules);
		startActivity(intent);
	}

	/**
	 * 获取首页活动list
	 */
	int i = 0;
	private List<MainActivityList> endList=new ArrayList<MainActivityList>();
	private void setActivityList(String activityType) {

		if (mActivityRequest == null) {
			mActivityRequest = new ActivityNetRequest(mContext);
		}
		String cityId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, mContext, "0");
		String countyId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, mContext, "0");
		mActivityRequest.getActivityList(activityType, cityId, countyId, pageIndex, ActivityList.class,
				new RequestCallBack<ActivityList>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        startMyDialog();
                    }

                    @Override
					public void onSuccess(ResponseInfo<ActivityList> arg0) {
						refreshable_view.onRefreshComplete();
//						ActivityList activityList = arg0.result;
						if (CommonUtils.NetworkRequestReturnCode(mContext, arg0.result.code + "")) {
							endList.clear();
							List<MainActivityList> activityList=arg0.result.getList();
							for (int i=0;i<activityList.size();++i) {
								MainActivityList activity=activityList.get(i);
								if (Constants.ACTIVITY_TYPE_SHAKE.equals(activity.getActivity_type())) {
									if ("2".equals(activity.getActivity_status())) {
										endList.add(activity);
									}
								}else {
									if (Constants.ACTIVITY_STATE_END.equals(activity.getActivity_status())) {
										endList.add(activity);
									}
								}
							}
							activityList.removeAll(endList);
							activityList.addAll(endList);
							lvActivity.setAdapter(new activityAdapter(ListActivity.this, activityList));
							CommonUtils.setListViewHeightBasedOnChildren(lvActivity);
							lvActivity.setVisibility(View.VISIBLE);
							mImgNoData.setVisibility(View.GONE);
						} else {
							lvActivity.setVisibility(View.GONE);
							mImgNoData.setVisibility(View.VISIBLE);
						}
						stopMyDialog();

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						stopMyDialog();
						refreshable_view.onRefreshComplete();
					}
				});
	}

	class activityAdapter extends BaseAdapter {
		private List<MainActivityList> mainActivitys;
		private Context context;

		public activityAdapter(Context context, List<MainActivityList> mList) {

			this.context = context;
			this.mainActivitys = mList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mainActivitys.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mainActivitys.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			BitmapUtils bitmapUtils = new BitmapUtils(context);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.listview_main_acitivity, null);

				holder.linearlayout_one = (LinearLayout) convertView.findViewById(R.id.linearlayout_one);
				holder.activity_iv_prize = (ImageView) convertView.findViewById(R.id.activity_iv_prize);
				holder.activity_name = (TextView) convertView.findViewById(R.id.activity_name);
				holder.activity_sponsor_name = (TextView) convertView.findViewById(R.id.activity_sponsor_name);
				holder.activity_start_time = (TextView) convertView.findViewById(R.id.activity_start_time);
				holder.activity_status = (TextView) convertView.findViewById(R.id.activity_status);
				holder.yinzhang_iv = (ImageView) convertView.findViewById(R.id.yinzhang_iv);

				holder.linearlayout_two = (LinearLayout) convertView.findViewById(R.id.linearlayout_two);
				holder.activity_prize_url = (CircleImageView1) convertView.findViewById(R.id.activity_prize_url);
				holder.activity_name_guess = (TextView) convertView.findViewById(R.id.activity_name_guess);
				holder.activity_start_time_guess = (TextView) convertView.findViewById(R.id.activity_start_time_guess);
				holder.activity_sponsor = (TextView) convertView.findViewById(R.id.activity_sponsor);
				holder.item_pro = (ProgressBar) convertView.findViewById(R.id.item_pro);
				holder.activity_play_count = (TextView) convertView.findViewById(R.id.activity_play_count);
				holder.activity_total_count = (TextView) convertView.findViewById(R.id.activity_total_count);
				holder.activity_other_count = (TextView) convertView.findViewById(R.id.activity_other_count);
				holder.yinzhang_iv_two = (ImageView) convertView.findViewById(R.id.yinzhang_iv_two);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (this.mainActivitys != null) {

				MainActivityList activity = mainActivitys.get(position);

				if (activity.getActivity_type().equals("2")) {// 活动类型是大家猜
					holder.linearlayout_one.setVisibility(View.GONE);
					holder.linearlayout_two.setVisibility(View.VISIBLE);

					bitmapUtils.display(holder.activity_prize_url,
							Constants.ImageUrl + activity.getActivity_prize_url() + Constants.ImageSuffix);
					holder.activity_name_guess.setText(activity.getActivity_name());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = sdf.format(new Date(Long.valueOf(activity.getActivity_start_time() + "000")))
							.substring(0, 10);
					holder.activity_start_time_guess.setText(time + "发布");
					holder.activity_sponsor.setText(activity.getActivity_sponsor_name());
					holder.item_pro.setMax(activity.getActivity_total_count());
					holder.item_pro.setProgress(activity.getActivity_play_count());
					holder.activity_play_count.setText(activity.getActivity_play_count() + "");
					holder.activity_total_count.setText(activity.getActivity_total_count() + "");
					holder.activity_other_count
							.setText(activity.getActivity_total_count() - activity.getActivity_play_count() + "");

					if (activity.getActivity_status().equals("3")) {
						holder.yinzhang_iv_two.setVisibility(View.VISIBLE);
					} else {
						holder.yinzhang_iv_two.setVisibility(View.GONE);
					}

				} else {

					holder.linearlayout_one.setVisibility(View.VISIBLE);
					holder.linearlayout_two.setVisibility(View.GONE);

					if (holder.activity_iv_prize != null) {
						bitmapUtils.display(holder.activity_iv_prize,
								Constants.ImageUrl + activity.getActivity_prize_url() + Constants.ImageSuffix);
					}
					if (holder.activity_name != null) {
						holder.activity_name.setText(activity.getActivity_name());
					}
					if (holder.activity_start_time != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String startTime = sdf
								.format(new Date(Long.valueOf(activity.getActivity_start_time() + "000")));
						holder.activity_start_time.setText(startTime);
					}
					if (holder.activity_status != null) {
						if (Constants.ACTIVITY_TYPE_SHAKE.equals(activity.getActivity_type())){
							if (activity.getActivity_start_time().compareTo(activity.getActivity_server_time())<=0){
								holder.activity_status.setText("（正在进行中）");
								holder.activity_status.setVisibility(View.VISIBLE);
								holder.yinzhang_iv_two.setVisibility(View.GONE);
							}else {
								holder.activity_status.setText("（活动未开始）");
								holder.activity_status.setVisibility(View.VISIBLE);
								holder.yinzhang_iv_two.setVisibility(View.GONE);
							}
							if ("2".equals(activity.getActivity_status())){
								holder.activity_status.setVisibility(View.GONE);
								holder.yinzhang_iv.setVisibility(View.VISIBLE);
							}
						}else {
							if (Constants.ACTIVITY_STATE_NOT_START.equals(activity.getActivity_status())) {
								holder.activity_status.setText("（活动未开始）");
								holder.activity_status.setVisibility(View.VISIBLE);
								holder.yinzhang_iv_two.setVisibility(View.GONE);
							} else if (Constants.ACTIVITY_STATE_GOING.equals(activity.getActivity_status())) {
								holder.activity_status.setText("（正在进行中）");
								holder.activity_status.setVisibility(View.VISIBLE);
								holder.yinzhang_iv_two.setVisibility(View.GONE);
							} else if ("3".equals(activity.getActivity_status())) {
								holder.activity_status.setVisibility(View.GONE);
								holder.yinzhang_iv.setVisibility(View.VISIBLE);
							}

						}
					}
					if (holder.activity_sponsor_name != null) {
						holder.activity_sponsor_name.setText(activity.getActivity_sponsor_name());
					}

					
				}

			}
			return convertView;
		}
	}

	private static class ViewHolder {
		LinearLayout linearlayout_one;
		ImageView activity_iv_prize;
		TextView activity_name;
		TextView activity_sponsor_name;
		TextView activity_start_time;
		TextView activity_status;
		ImageView yinzhang_iv;

		LinearLayout linearlayout_two;
		CircleImageView1 activity_prize_url;
		TextView activity_name_guess;
		TextView activity_start_time_guess;
		TextView activity_sponsor;
		ProgressBar item_pro;
		TextView activity_play_count;
		TextView activity_total_count;
		TextView activity_other_count;
		ImageView yinzhang_iv_two;
	}

}
