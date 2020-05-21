package com.yilian.mall.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshBase.Mode;
import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.RecordLebipayListBean;
import com.yilian.mall.entity.RecordLebipayListBean.LebipayListBean;
import com.yilian.mall.http.UserdataNetRequest;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 乐币支付记录
 * 
 * @author Administrator
 *
 */
public class RecordLebipayActivity extends BaseActivity {
	private static final String TAG = "RecordLebipayActivity============";
	private List<LebipayListBean> lebipayList;
	private UserdataNetRequest recordNetRequest;
	private BitmapUtils bitmapUtils;

	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.no_lebipay)
	private LinearLayout no_lebipay;
	@ViewInject(R.id.lebipay_listview)
	private PullToRefreshListView lebipay_listview;
//	@ViewInject(R.id.re_loading)
//	private Button re_loading;
	/**
	 * 按时间排序
	 */
/*	private void orderByTime() {
		Collections.sort(lebipayList, new Comparator<LebipayListBean>() {

			@Override
			public int compare(LebipayListBean lhs, LebipayListBean rhs) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d1 = sdf.format(new Date(Long.valueOf(lhs.deal_time + "000")));
				String d2 = sdf.format(new Date(Long.valueOf(rhs.deal_time+ "000")));
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
	private RecordLebipayAdapter recordLebipayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_lebipay_list);
		ViewUtils.inject(this);
        tv_back.setText("奖励支付记录");

		lebipayList = new ArrayList<LebipayListBean>();
		bitmapUtils = new BitmapUtils(mContext);

		initListView();

		getLebipayRequest();

//		re_loading.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				re_loading.setVisibility(View.GONE);
//				lebipay_listview.setVisibility(View.VISIBLE);
//				getLebipayRequest();
//
//			}
//		});
	}

	private void initListView() {
		lebipay_listview.setMode(Mode.PULL_FROM_START);
		lebipay_listview.getLoadingLayoutProxy(true, false).setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));
		lebipay_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getLebipayRequest();
			}
		});

	}
	
	private void getLebipayRequest() {
		startMyDialog();
		if (recordNetRequest == null) {
			recordNetRequest = new UserdataNetRequest(mContext);
		}
		recordNetRequest.getLebipayRecordList(RecordLebipayListBean.class, new RequestCallBack<RecordLebipayListBean>() {

			@Override
			public void onSuccess(ResponseInfo<RecordLebipayListBean> arg0) {
				stopMyDialog();
//				re_loading.setVisibility(View.GONE);
				lebipay_listview.setVisibility(View.VISIBLE);
				switch (arg0.result.code) {
				case 1:
					lebipayList = arg0.result.list;
//					orderByTime();

					if (lebipayList.size() == 0 || lebipayList == null) {
						no_lebipay.setVisibility(View.VISIBLE);
						lebipay_listview.setVisibility(View.GONE);
					}else {
						no_lebipay.setVisibility(View.GONE);
						lebipay_listview.setVisibility(View.VISIBLE);
					}

					recordLebipayAdapter = new RecordLebipayAdapter(mContext, lebipayList);
					lebipay_listview.setAdapter(recordLebipayAdapter);

					break;

				default:
					break;
				}
				lebipay_listview.onRefreshComplete();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {

				stopMyDialog();
//				re_loading.setVisibility(View.VISIBLE);
//				lebipay_listview.setVisibility(View.GONE);
				lebipay_listview.onRefreshComplete();

			}
		});

	}

	class RecordLebipayAdapter extends BaseAdapter{
		
		private List<LebipayListBean> lebipayListBeans;
		private Context context;
		private RecordLebipayAdapter(Context context, List<LebipayListBean> lebipayListBeans){
			this.context = context;
			this.lebipayListBeans = lebipayListBeans;
		}

		@Override
		public int getCount() {
			if (lebipayListBeans != null) {
				return lebipayListBeans.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return lebipayListBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.activity_record_lebipay_item, null);
				viewHolder.lebi_time_title = (TextView) convertView.findViewById(R.id.lebi_time_title);
				viewHolder.merchant_image = (CircleImageView1) convertView.findViewById(R.id.merchant_image);
				viewHolder.merchant_name = (TextView) convertView.findViewById(R.id.merchant_name);
				viewHolder.consumer_expend_lebi = (TextView) convertView.findViewById(R.id.consumer_expend_lebi);
				viewHolder.deal_time = (TextView) convertView.findViewById(R.id.deal_time);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			if (lebipayListBeans != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH : mm : ss");
				String time = sdf.format(new Date(Long.valueOf(lebipayListBeans.get(position).deal_time + "000")));
				String oldTime;
				try {
					oldTime = sdf.format(new Date(Long.valueOf(lebipayListBeans.get(position-1).deal_time + "000"))).substring(0,14);
				} catch (ArrayIndexOutOfBoundsException e) {
					oldTime = "";
				}
				if (viewHolder.merchant_name != null) {
					String str = lebipayListBeans.get(position).merchant_name;//"联盟商家联盟商家联盟商家联盟商家";
					if (str.length()>12) {
						viewHolder.merchant_name.setText(str.substring(0, 12)+" ···");
						
					}else {
						viewHolder.merchant_name.setText(str);
					}
				}
				if (viewHolder.consumer_expend_lebi != null) {
					viewHolder.consumer_expend_lebi.setText("- "+Float.valueOf(lebipayListBeans.get(position).consumer_expend_lebi)/100);
				}

				if (viewHolder.merchant_image != null) {
					bitmapUtils.display(viewHolder.merchant_image, Constants.ImageUrl + lebipayListBeans.get(position).merchant_image+Constants.ImageSuffix);
				}
				if (viewHolder.lebi_time_title != null) {
					viewHolder.lebi_time_title.setText(time.substring(0, 14));
				}
				if (viewHolder.deal_time != null) {
					viewHolder.deal_time.setText(time.substring(15, time.length()));
				}
				if (oldTime.equals(time.subSequence(0, 14))) {
					viewHolder.lebi_time_title.setVisibility(View.GONE);
				}else {
					viewHolder.lebi_time_title.setVisibility(View.VISIBLE);
				}
			}
			
			return convertView;
		}
		
		class ViewHolder{
			public TextView merchant_name;//商家名字
			public CircleImageView1 merchant_image;//商家照片
			public TextView consumer_expend_lebi;//消费乐币数量
			public TextView deal_time;//交易时间
			private TextView lebi_time_title;//日期
		}
	}
}
