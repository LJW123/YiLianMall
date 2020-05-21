package com.yilian.mall.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.RecordObtainJifenListBean;
import com.yilian.mall.entity.RecordObtainJifenListBean.JifenRecordListBean;
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
 * 奖券记录list界面
 */
public class RecordJifenObtainActivity extends BaseActivity {

	private static final String TAG = "RecordJifenObtainActivity";
	private List<JifenRecordListBean> jifenRecordList;
	private ServiceNetRequest serviceNetRequest;

	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.no_jifenobtain)
	private ImageView no_jifenobtain;
	@ViewInject(R.id.lv_jifenrecord_activity)
	private PullToRefreshListView lv_jifenrecord_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_obtainjifen_list);
		ViewUtils.inject(this);
		tv_back.setText("乐分币获得记录明细");

		jifenRecordList = new ArrayList<JifenRecordListBean>();
		initListView();

		getRecordList();

	}

	private void initListView() {
		lv_jifenrecord_activity.setMode(Mode.PULL_FROM_START);
		lv_jifenrecord_activity.getLoadingLayoutProxy(true, false).setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));
		lv_jifenrecord_activity.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getRecordList();
			}
		});
	}

	public void getRecordList() {
		startMyDialog();

		if (serviceNetRequest == null) {
			serviceNetRequest = new ServiceNetRequest(mContext);
		}
		serviceNetRequest.getJifenobtainRecordList(RecordObtainJifenListBean.class,
				new RequestCallBack<RecordObtainJifenListBean>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						stopMyDialog();
						lv_jifenrecord_activity.onRefreshComplete();
					}

					@Override
					public void onSuccess(ResponseInfo<RecordObtainJifenListBean> arg0) {
						stopMyDialog();
						switch (arg0.result.code) {
						case 1:
							jifenRecordList = arg0.result.list;

//							orderByTime();

							if (jifenRecordList.size() == 0 || jifenRecordList == null) {
								no_jifenobtain.setVisibility(View.VISIBLE);
								lv_jifenrecord_activity.setVisibility(View.GONE);
							} else {
								no_jifenobtain.setVisibility(View.GONE);
								lv_jifenrecord_activity.setVisibility(View.VISIBLE);
							}
							jifenRecordAdapter = new JifenRecordAdapter(mContext, jifenRecordList);
							lv_jifenrecord_activity.setAdapter(jifenRecordAdapter);
							break;

						default:
							break;
						}

						lv_jifenrecord_activity.onRefreshComplete();
					}

				});
	}

	/**
	 * 按时间排序
	 */
/*	private void orderByTime() {
		Collections.sort(jifenRecordList, new Comparator<JifenRecordListBean>() {

			@Override
			public int compare(JifenRecordListBean lhs, JifenRecordListBean rhs) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d1 = sdf.format(new Date(Long.valueOf(lhs.merchant_give_time + "000")));
				String d2 = sdf.format(new Date(Long.valueOf(rhs.merchant_give_time + "000")));
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
	}*/

	private JifenRecordAdapter jifenRecordAdapter;

	private class JifenRecordAdapter extends BaseAdapter {

		private List<JifenRecordListBean> jifenList;
		private Context context;

		public JifenRecordAdapter(Context context, List<JifenRecordListBean> jList) {

			this.context = context;
			this.jifenList = jList;
		}

		@Override
		public int getCount() {
			return jifenList.size();

		}

		@Override
		public Object getItem(int position) {
			return jifenList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.activity_record_obtainjifen_item, null);
				holder.merchant_name = (TextView) convertView.findViewById(R.id.merchant_name);
				holder.merchant_goods = (TextView) convertView.findViewById(R.id.merchant_goods);
				holder.merchant_income = (TextView) convertView.findViewById(R.id.merchant_income);
				holder.merchant_give_integral = (TextView) convertView.findViewById(R.id.merchant_give_integral);
				holder.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
				holder.time_title = (TextView) convertView.findViewById(R.id.time_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (jifenList != null) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH : mm : ss");

				String time = sdf.format(new Date(Long.valueOf(jifenList.get(position).merchant_give_time + "000")));

				String oldtime;
				try {
					oldtime = sdf.format(new Date(Long.valueOf(jifenList.get(position - 1).merchant_give_time + "000")))
							.substring(0, 14);
				} catch (ArrayIndexOutOfBoundsException e) {
					oldtime = "";
				}

				if (holder.merchant_name != null) {
					holder.merchant_name.setText(jifenList.get(position).merchant_name);
				}
				if (holder.merchant_goods != null) {
					holder.merchant_goods.setText("( " + jifenList.get(position).merchant_goods + " )");
				}
				if (holder.merchant_income != null) {
					if (jifenList.get(position).merchant_income.equals("0")
							|| jifenList.get(position).merchant_income.equals(" ")
							|| jifenList.get(position).merchant_income.equals("")) {
						holder.merchant_income.setText("- 0.00");
					} else {
						holder.merchant_income.setText("- " + Float.parseFloat(jifenList.get(position).merchant_income) / 100);
					}
				}
				if (holder.merchant_give_integral != null) {
					holder.merchant_give_integral.setText(jifenList.get(position).merchant_give_integral);
				}
				if (holder.tv_data != null) {
					holder.tv_data.setText(time.subSequence(15, time.length()));
				}
				if (holder.time_title != null) {
					holder.time_title.setText(time.subSequence(0, 14));
				}
				if (oldtime.equals(time.subSequence(0, 14))) {
					holder.time_title.setVisibility(View.GONE);
				} else {
					holder.time_title.setVisibility(View.VISIBLE);
				}
			}

			return convertView;
		}
	}

	private static class ViewHolder {
		TextView merchant_name;
		TextView merchant_goods;
		TextView merchant_income;
		TextView merchant_give_integral;
		TextView tv_data;
		TextView time_title;
	}

	// class list implements Comparator{
	//
	// @Override
	// public int compare(Object lhs, Object rhs) {
	// JifenRecordListBean bean0 = (JifenRecordListBean)lhs;
	// JifenRecordListBean bean1 = (JifenRecordListBean)rhs;
	// int flag = bean0.merchant_give_time.compareTo(bean1.merchant_give_time);
	// return flag;
	// }
	//
	// }
}
