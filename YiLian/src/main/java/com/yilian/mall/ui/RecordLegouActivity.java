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
import com.yilian.mall.entity.RecordLegouListBean;
import com.yilian.mall.entity.RecordLegouListBean.LegouListBean;
import com.yilian.mall.http.UserdataNetRequest;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 乐购记录
 */
public class RecordLegouActivity extends BaseActivity{
	private List<LegouListBean> legouList;
	private UserdataNetRequest recordNetRequest;
	private BitmapUtils bitmapUtils;
	
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	@ViewInject(R.id.no_legou)
	private LinearLayout no_legou;
	@ViewInject(R.id.legou_listview)
	private PullToRefreshListView legou_listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_legou_list);
		ViewUtils.inject(this);
		tv_back.setText("乐购记录");
		legouList = new ArrayList<LegouListBean>();
		
		bitmapUtils = new BitmapUtils(mContext);
		
		initListView();
		
		getLegouRequest();
	}
	private void initListView(){
		legou_listview.setMode(Mode.PULL_FROM_START);
		legou_listview.getLoadingLayoutProxy(true, false).setLoadingDrawable(getResources().getDrawable(R.drawable.pull_down_refresh));
		legou_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getLegouRequest();
			}
		});
	}
	
	private void getLegouRequest(){
		startMyDialog();
		if (recordNetRequest == null) {
			recordNetRequest = new UserdataNetRequest(mContext);
		}
		recordNetRequest.getLegouRecordList(RecordLegouListBean.class, new RequestCallBack<RecordLegouListBean>() {
			
			@Override
			public void onSuccess(ResponseInfo<RecordLegouListBean> arg0) {
				stopMyDialog();
				switch (arg0.result.code) {
				case 1:
					legouList = arg0.result.list;
					orderByTime();
					

					if (legouList.size() == 0 || legouList == null) {
						no_legou.setVisibility(View.VISIBLE);
						legou_listview.setVisibility(View.GONE);
					}else {
						no_legou.setVisibility(View.GONE);
						legou_listview.setVisibility(View.VISIBLE);
					}
					recordLegouAdapter = new RecordLegouAdapter(mContext,legouList);
					legou_listview.setAdapter(recordLegouAdapter);
					break;

				default:
					break;
				}
				
				legou_listview.onRefreshComplete();
				
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				stopMyDialog();
				legou_listview.onRefreshComplete();
			}
		});
	}
	
	/**
	 * 按时间排序
	 */
	private void orderByTime() {
		Collections.sort(legouList, new Comparator<LegouListBean>() {

			@Override
			public int compare(LegouListBean lhs, LegouListBean rhs) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d1 = sdf.format(new Date(Long.valueOf(lhs.payment_succeed_time + "000")));
				String d2 = sdf.format(new Date(Long.valueOf(rhs.payment_succeed_time + "000")));
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
	
	private RecordLegouAdapter recordLegouAdapter;
	private class RecordLegouAdapter extends BaseAdapter{
		
		private List<LegouListBean> legouListBeans;
		private Context context;
		private RecordLegouAdapter(Context context, List<LegouListBean> legouListBeans){
			this.context = context;
			this.legouListBeans = legouListBeans;
		}
		@Override
		public int getCount() {
			if (legouListBeans != null) {
				return legouListBeans.size();
			}
			return 0;
		}
		@Override
		public Object getItem(int position) {
			
			return legouListBeans.get(position);
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
				convertView = LayoutInflater.from(context).inflate(R.layout.activity_record_legou_item, null);
				viewHolder.legou_time_title = (TextView) convertView.findViewById(R.id.legou_time_title);
				viewHolder.shop_image = (CircleImageView1) convertView.findViewById(R.id.shop_image);
				viewHolder.shop_name = (TextView) convertView.findViewById(R.id.shop_name);
				viewHolder.consumer_gain_rmb = (TextView) convertView.findViewById(R.id.consumer_gain_rmb);
				viewHolder.consumer_gain_lebi = (TextView) convertView.findViewById(R.id.consumer_gain_lebi);
				viewHolder.legou_time = (TextView) convertView.findViewById(R.id.legou_time);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (legouListBeans != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH : mm : ss");
				String time = sdf.format(new Date(Long.valueOf(legouListBeans.get(position).payment_succeed_time + "000")));
				String oldTime;
				try {
					oldTime = sdf.format(new Date(Long.valueOf(legouListBeans.get(position-1).payment_succeed_time + "000"))).substring(0,14);
				} catch (ArrayIndexOutOfBoundsException e) {
					oldTime = "";
				}
				if (viewHolder.shop_name != null) {
					String str = legouListBeans.get(position).shop_name;
					if (str.length()>12) {
						
						viewHolder.shop_name.setText(str.substring(0, 12)+" ···");
					}else {
						viewHolder.shop_name.setText(str);
					}
				}

				if (viewHolder.consumer_gain_rmb != null) {
					viewHolder.consumer_gain_rmb.setText("- "+Float.parseFloat(legouListBeans.get(position).consumer_gain_lebi)/100);
				}
				if (viewHolder.consumer_gain_lebi != null) {
					viewHolder.consumer_gain_lebi.setText("+ "+Float.valueOf(legouListBeans.get(position).consumer_gain_lebi)/100);
				}
				if (viewHolder.shop_image != null) {
					bitmapUtils.display(viewHolder.shop_image, Constants.ImageUrl + legouListBeans.get(position).shop_image+Constants.ImageSuffix);
				}
				if (viewHolder.legou_time_title != null) {
					viewHolder.legou_time_title.setText(time.substring(0, 14));
				}
				if (viewHolder.legou_time != null) {
					viewHolder.legou_time.setText(time.substring(15, time.length()));
				}
				if (oldTime.equals(time.subSequence(0, 14))) {
					viewHolder.legou_time_title.setVisibility(View.GONE);
				}else {
					viewHolder.legou_time_title.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}
		private class ViewHolder{
			private TextView legou_time_title;//日期
			private CircleImageView1 shop_image;//分店环境照片
			private TextView shop_name;//兑换中心分店名字
			private TextView consumer_gain_rmb;//在本次交易中获得的乐币数量，同时代表花费的现金数量
			private TextView consumer_gain_lebi;//在本次交易中获得的乐币数量，同时代表花费的现金数量
			private TextView legou_time;//交易时间
		}
	}
}
