package com.yilian.mall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.yilian.mall.entity.ShakePrize;
import com.yilian.mall.entity.ShakePrize.PrizeList;
import com.yilian.mall.http.ActivityNetRequest;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * 奖品列表
 * @author lauyk
 */
public class PrizeListActivity extends BaseActivity{

	@ViewInject(R.id.lv_prize_details)
	private ListView lvPrizeDetails;
	
	ArrayList<PrizeList> prizelists; 
	
	@ViewInject(R.id.tv_back)
	private TextView tvBack;
	
	ActivityNetRequest activitNetRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prize_details);
		ViewUtils.inject(this);
		activitNetRequest = new ActivityNetRequest(mContext);
		setPrizeDetails(this.getIntent().getStringExtra("activity_index"));
		tvBack.setText("奖品列表");
		Listener();
		lvPrizeDetails.setOverScrollMode(View.OVER_SCROLL_NEVER);
	}

	private void Listener() {
		lvPrizeDetails.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrizeList itemAtPosition = (PrizeList) parent.getItemAtPosition(position);
                Intent intent = null;
                if(itemAtPosition.prizeSponsorType.equals("0")){//跳转联盟商家
                    intent = new Intent(PrizeListActivity.this, MTMerchantDetailActivity.class);
					intent.putExtra("itemType", "1");
				}else if(itemAtPosition.prizeSponsorType.equals("2")){//跳转兑换中心
                   intent = new Intent(PrizeListActivity.this, MerchantActivity.class);
					intent.putExtra("itemType", "3");
				}
				intent.putExtra("merchant_id", itemAtPosition.prizeSponsor);
				startActivity(intent);
			}
		});
		
	}

	void setPrizeDetails(String index){
		startMyDialog();
		activitNetRequest.getShakePrize(index, new RequestCallBack<ShakePrize>() {
			
			@Override
			public void onSuccess(ResponseInfo<ShakePrize> arg0) {
				
				switch (arg0.result.code) {
				case 1:
					
					lvPrizeDetails.setAdapter(new PrizeListAdapter(mContext, arg0.result.list));
					stopMyDialog();
					break;

				default:
					showToast("获取失败~");
					stopMyDialog();
					break;
				}
				
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				stopMyDialog();
			}
		});

	};
	
	
	class PrizeListAdapter extends BaseAdapter{
		private ArrayList<PrizeList> mList;
		private Context context;
		public PrizeListAdapter(Context context,ArrayList<PrizeList> mList) {  

			this.context = context;  
			this.mList = mList;
		}  


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			BitmapUtils bitmapUtils = new BitmapUtils(context);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.listitem_prizelist, null);
				holder.ivPrize = (ImageView) convertView.findViewById(R.id.iv_prize);
				holder.ivStoreTel = (ImageView) convertView.findViewById(R.id.iv_store_tel);
				holder.prizeName = (TextView) convertView.findViewById(R.id.prize_name);
				holder.storeName = (TextView) convertView.findViewById(R.id.store_sponsor_name);
				holder.storeAddress = (TextView) convertView.findViewById(R.id.store_address);
				holder.prizeAmount = (TextView) convertView.findViewById(R.id.prize_amount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

            final PrizeList prizeList = mList.get(position);
            if (this.mList != null) {
				if (holder.ivPrize!= null) {
					bitmapUtils.display(holder.ivPrize, Constants.ImageUrl+ prizeList.prizeUrl+Constants.ImageSuffix);
				}
				if (holder.prizeName!= null) {
					holder.prizeName.setText(prizeList.prizeName);
				}
				if (holder.storeName!= null) {
					holder.storeName.setText(prizeList.prizeSponsorName);
				}
				if (holder.storeAddress!= null) {
					holder.storeAddress.setText(prizeList.prizeAddress);
				}
				if (holder.prizeAmount!= null) {
					holder.prizeAmount.setText("数量  :  ×"+ prizeList.prizeAmount);
				}
				
				
			}
			
			holder.ivStoreTel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 获取电话，判断电话号码是否存在。
					final String tel = prizeList.prizeTel;
					if (tel!=null&&tel.length() > 0) {

						showDialog(null, tel, null, 0, Gravity.CENTER, "拨打", "取消", true,
								new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									dialog.dismiss();
									Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
									context.startActivity(intent);
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									dialog.dismiss();
									break;
								}

							}
						}, context);

					} else {
						showToast("没有电话号码~");
					}
					
				}
			});
			return convertView;
		}
	}
	
	private static class ViewHolder {
		/**
		 * 奖品图片
		 */
		ImageView ivPrize;
		/**
		 * 商家电话
		 */
		ImageView ivStoreTel;
		/**
		 * 奖品名字
		 */
		TextView prizeName;
		/**
		 * 商家名字
		 */
		TextView storeName;
		/**
		 * 领奖弟子
		 */
		TextView storeAddress;
		/**
		 * 奖品数量
		 */
		TextView prizeAmount;
	}


}
