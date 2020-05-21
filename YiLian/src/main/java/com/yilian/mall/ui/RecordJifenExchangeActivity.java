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
import com.yilian.mall.entity.RecordExchangeJifenListBean;
import com.yilian.mall.entity.RecordExchangeJifenListBean.ExchangeJifenListBean;
import com.yilian.mall.http.ServiceNetRequest;
import com.yilian.mall.utils.DateUtils;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 奖券兑换记录list界面
 */
public class RecordJifenExchangeActivity extends BaseActivity {

	private List<ExchangeJifenListBean> exchangeJifenListBean;

	private ServiceNetRequest serviceNetRequest;

	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.no_exchangejifen)
	private ImageView no_exchangejifen;
	@ViewInject(R.id.lv_exchange_jifenrecord_activity)
	private PullToRefreshListView lv_exchange_jifenrecord_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_exchangejifen_list);
		ViewUtils.inject(this);
		tv_back.setText("乐分币兑换记录明细");
		exchangeJifenListBean = new ArrayList<ExchangeJifenListBean>();

		initListView();
		getJifenExchangeRecordList();

	}

	private void initListView() {
		lv_exchange_jifenrecord_activity.setMode(Mode.PULL_FROM_START);
		lv_exchange_jifenrecord_activity.getLoadingLayoutProxy(true, false).setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));
		lv_exchange_jifenrecord_activity.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getJifenExchangeRecordList();
			}
		});

		// lv_exchange_jifenrecord_activity.setMode(Mode.BOTH);
		// lv_exchange_jifenrecord_activity.setOnRefreshListener(new
		// OnRefreshListener2<ListView>() {
		//
		// @Override
		// public void onPullDownToRefresh(PullToRefreshBase<ListView>
		// refreshView) {
		//
		// }
		//
		// @Override
		// public void onPullUpToRefresh(PullToRefreshBase<ListView>
		// refreshView) {
		//
		// }
		//
		// });

	}

	private void getJifenExchangeRecordList() {
		startMyDialog();
		if (serviceNetRequest == null) {
			serviceNetRequest = new ServiceNetRequest(mContext);
		}

		serviceNetRequest.getJifenExchangeRecordList(RecordExchangeJifenListBean.class,
				new RequestCallBack<RecordExchangeJifenListBean>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						stopMyDialog();
						lv_exchange_jifenrecord_activity.onRefreshComplete();
					}

					@Override
					public void onSuccess(ResponseInfo<RecordExchangeJifenListBean> arg0) {
						stopMyDialog();
						switch (arg0.result.code) {
						case 1:
							exchangeJifenListBean = arg0.result.list;
//							orderByTime();

							if (exchangeJifenListBean.size() == 0 || exchangeJifenListBean == null) {
								no_exchangejifen.setVisibility(View.VISIBLE);
								lv_exchange_jifenrecord_activity.setVisibility(View.GONE);
							} else {
								no_exchangejifen.setVisibility(View.GONE);
								lv_exchange_jifenrecord_activity.setVisibility(View.VISIBLE);
							}
							exchangeJifenAdapter = new ExchangeJifenAdapter(mContext, exchangeJifenListBean);
							lv_exchange_jifenrecord_activity.setAdapter(exchangeJifenAdapter);

							break;

						default:
							break;
						}

						lv_exchange_jifenrecord_activity.onRefreshComplete();
					}
				});
	}

	/**
	 * 按时间排序
	 */
	private void orderByTime() {
		Collections.sort(exchangeJifenListBean, new Comparator<ExchangeJifenListBean>() {

			@Override
			public int compare(ExchangeJifenListBean lhs, ExchangeJifenListBean rhs) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d1 = sdf.format(new Date(Long.valueOf(lhs.sell_time + "000")));
				String d2 = sdf.format(new Date(Long.valueOf(rhs.sell_time + "000")));
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

	private ExchangeJifenAdapter exchangeJifenAdapter;

	private class ExchangeJifenAdapter extends BaseAdapter {

		private List<ExchangeJifenListBean> exchangeJifenList;
		private Context context;

		public ExchangeJifenAdapter(Context context, List<ExchangeJifenListBean> jList) {

			this.context = context;
			this.exchangeJifenList = jList;
		}

		@Override
		public int getCount() {
			return exchangeJifenList.size();
		}

		@Override
		public Object getItem(int position) {
			return exchangeJifenList.get(position);
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
				convertView = LayoutInflater.from(context).inflate(R.layout.activity_record_exchangejifen_item, null);
				holder.filiale_name = (TextView) convertView.findViewById(R.id.filiale_name);
				holder.sell_cash = (TextView) convertView.findViewById(R.id.sell_cash);
				holder.sell_total_integral = (TextView) convertView.findViewById(R.id.sell_total_integral);
				holder.sell_time = (TextView) convertView.findViewById(R.id.sell_time);
				holder.title_time = (TextView) convertView.findViewById(R.id.time_title);
				holder.line = (ImageView) convertView.findViewById(R.id.line);
				holder.linetop = convertView.findViewById(R.id.linetop);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (exchangeJifenList != null) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH : mm : ss");
				String time = sdf.format(new Date(Long.valueOf(exchangeJifenList.get(position).sell_time + "000")));
				String oldtime;
				try {
					oldtime = sdf.format(new Date(Long.valueOf(exchangeJifenList.get(position - 1).sell_time + "000")))
							.substring(0, 14);

				} catch (ArrayIndexOutOfBoundsException e) {
					oldtime = "";
				}
				if (holder.filiale_name != null) {
					holder.filiale_name.setText(exchangeJifenList.get(position).filiale_name);
				}
				if (holder.sell_cash != null) {
					holder.sell_cash.setText("- " + Float.parseFloat(exchangeJifenList.get(position).sell_cash)/100);
				}
				if (holder.sell_total_integral != null) {
					holder.sell_total_integral.setText("- " + Integer.parseInt(exchangeJifenList.get(position).sell_total_integral));
				}
				if (holder.sell_time != null) {
					holder.sell_time.setText(time.subSequence(15, time.length()));
				}
				if (holder.title_time != null) {
					holder.title_time.setText(time.subSequence(0, 14));
				}

				if (oldtime.equals(time.subSequence(0, 14))) {
					holder.title_time.setVisibility(View.GONE);
					holder.linetop.setVisibility(View.GONE);
				} else {
					holder.title_time.setVisibility(View.VISIBLE);
					holder.linetop.setVisibility(View.VISIBLE);
				}
			}

			return convertView;
		}
	}

	private static class ViewHolder {
		TextView filiale_name;
		TextView sell_cash;
		TextView sell_total_integral;
		TextView sell_time;
		TextView title_time;
		ImageView line;
		View linetop;
	}
}
