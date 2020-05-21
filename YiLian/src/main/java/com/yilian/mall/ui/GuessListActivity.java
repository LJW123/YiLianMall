package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.GuessAdapter;
import com.yilian.mall.entity.ActivityList;
import com.yilian.mall.entity.MainActivityList;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 大家猜活动列表
 */
public class GuessListActivity extends BaseActivity implements OnItemClickListener {

	private BitmapUtils bitmapUtils;
	private ActivityNetRequest activityNetRequest;
	List<MainActivityList> mainActivitys;
	private int pageIndex = 0;
	private String activityType;

	@ViewInject(R.id.tv_back)
	private TextView tvBack;

	@ViewInject(R.id.right_textview)
	private TextView rightTextView;

	@ViewInject(R.id.guess_list)
	private ListView guess_list;

	@ViewInject(R.id.no_activity)
	private ImageView no_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guesslist);
		ViewUtils.inject(this);
		startMyDialog();
		bitmapUtils = new BitmapUtils(mContext);
		mainActivitys = new ArrayList<MainActivityList>();
		activityType = this.getIntent().getStringExtra("activityType");
		initView();
		getValues();
	}

	public void initView() {
		tvBack.setText(this.getIntent().getStringExtra("activityName"));
		rightTextView.setText("活动规则");
		
		/*guess_list.setMode(Mode.PULL_FROM_START);
		guess_list.getLoadingLayoutProxy(true, false).setLoadingDrawable(getResources().getDrawable(R.anim.pull_down_refresh));
		guess_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				getValues();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			}
		});*/

		guess_list.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	public void rightTextview(View v) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra("title_name", getString(R.string.activity_rule));
		intent.putExtra("url", Constants.ActivityRules);
		startActivity(intent);
	}

	private List<MainActivityList> endList=new ArrayList<MainActivityList>();
	public void getValues() {

		if (activityNetRequest == null) {
			activityNetRequest = new ActivityNetRequest(mContext);
		}
		String cityId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, mContext, "0");
		String countyId = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, mContext, "0");

		activityNetRequest.getActivityList(activityType, cityId, countyId, pageIndex, ActivityList.class,
				new RequestCallBack<ActivityList>() {

					@Override
					public void onSuccess(ResponseInfo<ActivityList> arg0) {
						stopMyDialog();
//						guess_list.onRefreshComplete();
						switch (arg0.result.code) {
						case 1:
							endList.clear();
							List<MainActivityList> activityList=arg0.result.getList();
							for (int i=0;i<activityList.size();++i) {
								MainActivityList activity=activityList.get(i);
								if (Constants.ACTIVITY_STATE_END.equals(activity.getActivity_status())) {
									endList.add(activity);
								}
							}
							activityList.removeAll(endList);
							activityList.addAll(endList);
							/*if (mainActivitys.size() != 0) {
								no_activity.setVisibility(View.GONE);
								guess_list.setVisibility(View.VISIBLE);
							} else {
							}*/
							GuessAdapter guessAdapter = new GuessAdapter(GuessListActivity.this, activityList,bitmapUtils);
							guess_list.setAdapter(guessAdapter);
							break;
						case -21:
							no_activity.setVisibility(View.VISIBLE);
							guess_list.setVisibility(View.GONE);
							break;
						default:
							break;
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						stopMyDialog();
//						guess_list.onRefreshComplete();

					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MainActivityList activity = (MainActivityList) parent.getItemAtPosition(position);
		Intent intent = new Intent(this, GuessDetailActivity.class);
		intent.putExtra("activity_index",activity.getActivity_index());
		if ("3".equals(activity.getActivity_status())) {
			intent.putExtra("activity_state", activity.getActivity_status());
		}
		startActivity(intent);
	}
}
