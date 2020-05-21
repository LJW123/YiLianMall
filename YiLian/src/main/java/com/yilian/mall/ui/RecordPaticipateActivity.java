package com.yilian.mall.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.RecordPaticipateActivityListBean;
import com.yilian.mall.entity.RecordPaticipateActivityListBean.PaticipateActivityListBean;
import com.yilian.mall.http.ServiceNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshBase.Mode;
import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动参与记录list界面
 */
public class RecordPaticipateActivity extends BaseActivity {

	private List<PaticipateActivityListBean> paticipateActivityList;
	private ServiceNetRequest serviceNetRequest;

	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.no_paticipate)
	private ImageView no_paticipate;
	@ViewInject(R.id.lv_PaticipateActivity_record_activity)
	private PullToRefreshListView lv_PaticipateActivity_record_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_paticipateactivity_list);
		ViewUtils.inject(this);

		tv_back.setText("参与活动记录明细");

		paticipateActivityList = new ArrayList<PaticipateActivityListBean>();

		initListView();
		PaticipateActivityRecordList();

	}

	private void initListView() {
		lv_PaticipateActivity_record_activity.setMode(Mode.PULL_FROM_START);
		lv_PaticipateActivity_record_activity.getLoadingLayoutProxy(true, false)
				.setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));
		lv_PaticipateActivity_record_activity.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				PaticipateActivityRecordList();
			}
		});
	}

	private void PaticipateActivityRecordList() {
		startMyDialog();
		if (serviceNetRequest == null) {
			serviceNetRequest = new ServiceNetRequest(mContext);
		}

		serviceNetRequest.getPaticipateActivityRecordList(RecordPaticipateActivityListBean.class,
				new RequestCallBack<RecordPaticipateActivityListBean>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						stopMyDialog();
						lv_PaticipateActivity_record_activity.onRefreshComplete();

					}

					@Override
					public void onSuccess(ResponseInfo<RecordPaticipateActivityListBean> arg0) {
						stopMyDialog();
						switch (arg0.result.code) {
						case 1:
							paticipateActivityList = arg0.result.list;

							if (paticipateActivityList.size() == 0 || paticipateActivityList == null) {
								no_paticipate.setVisibility(View.VISIBLE);
								lv_PaticipateActivity_record_activity.setVisibility(View.GONE);
							} else {
								no_paticipate.setVisibility(View.GONE);
								lv_PaticipateActivity_record_activity.setVisibility(View.VISIBLE);
							}

							// orderByTime();

							lv_PaticipateActivity_record_activity.setAdapter(new PaticipateActivityAdapter(
									RecordPaticipateActivity.this, paticipateActivityList));

							break;

						default:
							break;
						}
						lv_PaticipateActivity_record_activity.onRefreshComplete();
					}
				});
	}

	/**
	 * 按时间排序
	 */
	/*
	 * private void orderByTime() {
	 * 
	 * Collections.sort(paticipateActivityList, new
	 * Comparator<PaticipateActivityListBean>() {
	 * 
	 * @Override public int compare(PaticipateActivityListBean lhs,
	 * PaticipateActivityListBean rhs) { SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String d1 = sdf.format(new
	 * Date(Long.valueOf(lhs.paticipate_time + "000"))); String d2 =
	 * sdf.format(new Date(Long.valueOf(rhs.paticipate_time + "000"))); Date
	 * date1 = DateUtils.stringToDate(d1); Date date2 =
	 * DateUtils.stringToDate(d2); if (date1 != null && date2 != null) { if
	 * (date1.before(date2)) { return 1; } } return -1; } }); }
	 */

	private class PaticipateActivityAdapter extends BaseAdapter {

		private List<PaticipateActivityListBean> paticipateActivityList;
		private Context context;

		public PaticipateActivityAdapter(Context context, List<PaticipateActivityListBean> pList) {

			this.context = context;
			this.paticipateActivityList = pList;
		}

		@Override
		public int getCount() {
			return paticipateActivityList.size();
		}

		@Override
		public Object getItem(int position) {
			return paticipateActivityList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(RecordPaticipateActivity.this, R.layout.activity_record_paticipateactiviey_item,
						null);
				holder = new ViewHolder();
				holder.activity_name = (TextView) view.findViewById(R.id.activity_name);
				holder.activity_pay = (TextView) view.findViewById(R.id.activity_pay);
				holder.paticipate_time = (TextView) view.findViewById(R.id.paticipate_time);
				holder.time_title = (TextView) view.findViewById(R.id.time_title);
				holder.line = (ImageView) view.findViewById(R.id.line);
				holder.linetop = view.findViewById(R.id.linetop);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			if (paticipateActivityList != null) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH : mm : ss");
				String time = sdf
						.format(new Date(Long.valueOf(paticipateActivityList.get(position).paticipate_time + "000")));
				// Log.e(TAG, time + "======================");
				String oldtime;

				try {
					oldtime = sdf
							.format(new Date(
									Long.valueOf(paticipateActivityList.get(position - 1).paticipate_time + "000")))
							.substring(0, 14);
				} catch (ArrayIndexOutOfBoundsException e) {
					oldtime = "";
				}
				if (holder.activity_name != null) {
					holder.activity_name.setText(paticipateActivityList.get(position).activity_name);
				}
				if (holder.activity_pay != null) {
					holder.activity_pay.setText("- " + paticipateActivityList.get(position).activity_pay);
				}
				if (holder.paticipate_time != null) {
					holder.paticipate_time.setText(time.subSequence(15, time.length()));
				}
				if (holder.time_title != null) {
					holder.time_title.setText(time.subSequence(0, 14));
				}
				if (oldtime.equals(time.subSequence(0, 14))) {
					holder.time_title.setVisibility(View.GONE);
					holder.linetop.setVisibility(View.GONE);
				} else {
					holder.time_title.setVisibility(View.VISIBLE);
					holder.linetop.setVisibility(View.VISIBLE);
				}
			}

			return view;
		}
	}

	private static class ViewHolder {
		TextView activity_name;
		TextView activity_pay;
		TextView paticipate_time;
		TextView time_title;
		ImageView line;
		View linetop;
	}

}
