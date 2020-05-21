package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yilian.mall.R;
import com.yilian.mall.entity.ActivityInfo;
import com.yilian.mall.entity.ActivityPrize;
import com.yilian.mylibrary.Constants;

import java.util.List;

public class PrizeListAdapter extends BaseAdapter {
	
	private Context mContext;
	BitmapUtils mBitmapUtil;
	private ActivityInfo mActivityInfo;

	public PrizeListAdapter(Context context, ActivityInfo activityInfo) {
		super();
		this.mContext = context;
		this.mActivityInfo=activityInfo;
		mBitmapUtil=new BitmapUtils(mContext);
		mActivityInfo=activityInfo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mActivityInfo!=null) {
			return 5;//1-5等奖
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mActivityInfo!=null&&mActivityInfo.prizes!=null&&position<=2) {
			return mActivityInfo.prizes.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_prize_list, null);
			viewHolder.txtPrizeLevel=(TextView) convertView.findViewById(R.id.txt_prize_level);
			viewHolder.txtPrizeName=(TextView) convertView.findViewById(R.id.txt_prize_name);
			viewHolder.imgPrizeIcon=(ImageView) convertView.findViewById(R.id.imgView_prize_icon);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if (position==3) {//四等奖
			viewHolder.txtPrizeLevel.setText(mContext.getString(R.string.prize_level4)+" (名额不限哦！)");
			viewHolder.txtPrizeName.setText(mActivityInfo.expend+"乐分币");
			viewHolder.imgPrizeIcon.setImageResource(R.mipmap.lefenbi);//setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.prize_lefen));
		}else if (position==4) {//五等奖
			viewHolder.txtPrizeLevel.setText(mContext.getString(R.string.prize_level5)+" (名额不限哦！)");
			viewHolder.txtPrizeName.setText(mActivityInfo.expend/2+"乐分币");
			viewHolder.imgPrizeIcon.setImageResource(R.mipmap.lefenbi);//setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.prize_lefen));
		}else {
			List<ActivityPrize> prizes = mActivityInfo.prizes;
			if (prizes != null) {
				ActivityPrize prize = prizes.get(position);
				if (prize != null) {
					String level = null;
					switch (prize.prizeLevel) {
						case 0:
							level = mContext.getResources().getString(R.string.prize_level1);
							break;
						case 1:
							level = mContext.getResources().getString(R.string.prize_level2);
							break;
						case 2:
							level = mContext.getResources().getString(R.string.prize_level3);
							break;

						default:
							break;
					}
					viewHolder.txtPrizeLevel.setText(level + " "
							+ mContext.getResources().getString(R.string.prize_hole_count, prize.prizeAmount));
					viewHolder.txtPrizeName.setText(prize.prizeName);
					mBitmapUtil.display(viewHolder.imgPrizeIcon,
							Constants.ImageUrl + prize.prizeImgUrl + Constants.ImageSuffix);
				}
			}
		}
		return convertView;
	}

	class ViewHolder{
		public TextView txtPrizeLevel;
		public ImageView imgPrizeIcon;
		public TextView txtPrizeName;
	}
}
