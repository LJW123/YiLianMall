package com.yilian.mall.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.PrizeVoucher;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.ImageLoaderUtil;
import com.yilian.mylibrary.Constants;

import java.util.Date;
import java.util.List;

public class PrizeVoucherListAdapter extends BaseAdapter {

	private Context mContext;
	private List<PrizeVoucher> mVouchers;
	private ImageLoaderUtil mImageLoaderUtil;
	private BitmapUtils mBitmapUtils;
	
	public PrizeVoucherListAdapter(Context context, List<PrizeVoucher> vouchers) {
		super();
		this.mContext = context;
		this.mVouchers = vouchers;
		mImageLoaderUtil=new ImageLoaderUtil(context);
		mBitmapUtils=new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mVouchers != null) {
			return mVouchers.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mVouchers != null) {
			return mVouchers.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_prize_vouchers, null);
			viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.imgView_icon);
			viewHolder.txtPrizeName = (TextView) convertView.findViewById(R.id.txt_prize_name);
			viewHolder.txtLove = (TextView) convertView.findViewById(R.id.txt_love);
			viewHolder.txtExchangeStatus = (TextView) convertView.findViewById(R.id.txt_exchang_status);
			viewHolder.txtDeadline=(TextView) convertView.findViewById(R.id.txt_deadline);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Logger.i("convertView", "convertView id=" + convertView.hashCode() + "|||position=" + position);
		final PrizeVoucher voucher = mVouchers.get(position);
		if (voucher != null) {
			viewHolder.txtPrizeName.setText(voucher.prizeName);
			viewHolder.txtLove.setText(voucher.activityName);
			Date deadline=new Date(voucher.validTime*1000);
			viewHolder.txtDeadline.setText("有效期至  "+DateFormat.format("yyyy-MM-dd", deadline));
			mBitmapUtils.display(viewHolder.imgIcon, Constants.ImageUrl+voucher.imgUrl+Constants.ImageSuffix,new BitmapLoadCallBack<View>() {

				@Override
				public void onLoadCompleted(View arg0, String arg1, Bitmap arg2, BitmapDisplayConfig arg3,
						BitmapLoadFrom arg4) {
					// TODO Auto-generated method stub
					if (voucher.status==1) {
						viewHolder.imgIcon.setImageBitmap(BitmapUtil.bitmap2Gray(arg2));
					}else if(voucher.status==2){
						viewHolder.imgIcon.setImageBitmap(BitmapUtil.bitmap2Gray(arg2));
					}else {
						viewHolder.imgIcon.setImageBitmap(arg2);
						
					}
				}

				@Override
				public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
					// TODO Auto-generated method stub
					
				}
			});
			Drawable drawable;
			if (voucher.status == 1) {
				viewHolder.txtExchangeStatus.setText("已兑换");
				drawable = mContext.getResources().getDrawable(R.mipmap.duihuanpingzheng_offdollar);
//				viewHolder.imgIcon.setDrawingCacheEnabled(true);
//				Bitmap srcBitmap=viewHolder.imgIcon.getDrawingCache();
//				viewHolder.imgIcon.setImageBitmap(BitmapUtil.bitmap2Gray(srcBitmap));
//				viewHolder.imgIcon.setDrawingCacheEnabled(false);
				
//				mImageLoaderUtil.displayImage2("http://cdn.image.huyongle.com/"+voucher.imgUrl, viewHolder.imgIcon, false);
				
				
			}else if(voucher.status==2){
				viewHolder.txtExchangeStatus.setText("已过期");
				drawable = mContext.getResources().getDrawable(R.mipmap.duihuanpingzheng_offdollar);
			} else {
//				mImageLoaderUtil.displayImage1("http://cdn.image.huyongle.com/"+voucher.imgUrl, viewHolder.imgIcon, false);
				viewHolder.txtExchangeStatus.setText("立即兑换");
				drawable = mContext.getResources().getDrawable(R.mipmap.duihuanpingzheng_dollar);
//				convertView.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						Intent intent=new Intent(mContext, PrizeVoucherDetailActivity.class);
//						intent.putExtra("voucher_index", voucher.id);
//						intent.putExtra("activity_name", voucher.activityName);
//						mContext.startActivity(intent);
//					}
//				});
				
			}
			
			int px = CommonUtils.dip2px(mContext, 20);
			drawable.setBounds(0, 0, px, px);//需要设置bounds，否则无法显示
			viewHolder.txtExchangeStatus.setCompoundDrawablesRelative(null, drawable, null, null);

		}
		return convertView;
	}

	class ViewHolder {
		ImageView imgIcon;
		TextView txtPrizeName;
		TextView txtLove;
		TextView txtDeadline;
		TextView txtExchangeStatus;
	}
}
