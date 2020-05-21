package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.GuessedAdapter;
import com.yilian.mall.entity.GuessedNumberEntity;
import com.yilian.mall.entity.GuessedNumberEntity.GuessedNumberEntityList;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 我猜过的数字界面
 */
public class GuessedListActivity extends BaseActivity {

	@ViewInject(R.id.tv_back)
	private TextView tvBack;

	@ViewInject(R.id.right_textview)
	private TextView rightTextView;
	
	@ViewInject(R.id.activity_name)
	private TextView activity_name;

	@ViewInject(R.id.no_guessed_number)
	private LinearLayout no_guessed_number;
	
	@ViewInject(R.id.guessed_scroll)
	private ScrollView guessed_scroll;
	
	@ViewInject(R.id.guessed_number_lv)
	private ListView guessed_number_lv;
	
	@ViewInject(R.id.guessed_list_ll)
	private LinearLayout guessed_list_ll;
	
	LinearLayout.LayoutParams guessed_list_ll_lp;
	
	private ActivityNetRequest activityNetRequest;
	private List<GuessedNumberEntityList> guessedNumberList;
	
	private String activity_index;//活动id
	private String activityName;//活动名称

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guessed_number);
		ViewUtils.inject(this);
		activity_index = getIntent().getStringExtra("activity_index");
		activityName = getIntent().getStringExtra("activity_name");
		guessedNumberList = new ArrayList<GuessedNumberEntityList>();
		 startMyDialog();
		initView();
		getValues();
	}

	public void initView() {
		guessed_list_ll_lp = (LayoutParams) guessed_list_ll.getLayoutParams();
		guessed_number_lv.setDividerHeight(0);
		rightTextView.setText("活动规则");
		tvBack.setText("我猜过的数字");
		activity_name.setText("活动名称:"+activityName);
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

	public void getValues() {

		startMyDialog();
		if ( activityNetRequest== null) {
			activityNetRequest = new ActivityNetRequest(mContext);
		}
		
		activityNetRequest.getGuessFigureList(activity_index, new RequestCallBack<GuessedNumberEntity>() {
			
			@Override
			public void onSuccess(ResponseInfo<GuessedNumberEntity> arg0) {
				stopMyDialog();
				switch (arg0.result.code) {
				case 1:
					guessedNumberList = arg0.result.list;
					orderByTime();
					if (guessedNumberList.size() != 0) {
						guessed_scroll.setVisibility(View.VISIBLE);
						no_guessed_number.setVisibility(View.GONE);
					}else {
						guessed_scroll.setVisibility(View.GONE);
						no_guessed_number.setVisibility(View.VISIBLE);
					}
					
					GuessedAdapter guessAdapter = new GuessedAdapter(GuessedListActivity.this, guessedNumberList);
					guessed_number_lv.setAdapter(guessAdapter);
					guessed_list_ll_lp.height = setListViewHeightBasedOnChildren(GuessedListActivity.this, guessed_number_lv);
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
	 * 按时间排序
	 */
	private void orderByTime() {
		Collections.sort(guessedNumberList, new Comparator<GuessedNumberEntityList>() {

			@Override
			public int compare(GuessedNumberEntityList lhs, GuessedNumberEntityList rhs) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d1 = sdf.format(new Date(Long.valueOf(lhs.guess_time + "000")));
				String d2 = sdf.format(new Date(Long.valueOf(rhs.guess_time + "000")));
				Date date1 = DateUtils.stringToDate(d1);
				Date date2 = DateUtils.stringToDate(d2);
				if (date1 != null && date2 != null) {
					if (date1.before(date2)) {
						return 1;
					}
				}
				return -1;
			}
		});
	}
	
	public int setListViewHeightBasedOnChildren(Context context, ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			return 0;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			listItem.setFocusable(false);
			listItem.setFocusableInTouchMode(false);
			listView.clearChildFocus(listItem);

		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
		params.height = totalHeight;
		params.width = LinearLayout.LayoutParams.FILL_PARENT;
		listView.setLayoutParams(params);
		listView.setFocusable(false);
		return params.height;
	}
}
