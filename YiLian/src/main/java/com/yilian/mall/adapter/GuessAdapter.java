package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yilian.mall.R;
import com.yilian.mall.entity.MainActivityList;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GuessAdapter extends BaseAdapter {

	private List<MainActivityList> activityLists;
	private Context context;
	private BitmapUtils bitmapUtils;

	public GuessAdapter(Context context, List<MainActivityList> activityLists, BitmapUtils bitmapUtils) {
		this.context = context;
		this.activityLists = activityLists;
		this.bitmapUtils = bitmapUtils;
	}

	@Override
	public int getCount() {
		if (activityLists != null) {
			return activityLists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return activityLists.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_guess, null);
			holder.activity_prize_url = (CircleImageView1) convertView.findViewById(R.id.activity_prize_url);
			holder.activity_name = (TextView) convertView.findViewById(R.id.activity_name);
			holder.activity_start_time = (TextView) convertView.findViewById(R.id.activity_start_time);
			holder.activity_sponsor = (TextView) convertView.findViewById(R.id.activity_sponsor);
			holder.item_pro = (ProgressBar) convertView.findViewById(R.id.item_pro);
			holder.activity_play_count = (TextView) convertView.findViewById(R.id.activity_play_count);
			holder.activity_total_count = (TextView) convertView.findViewById(R.id.activity_total_count);
			holder.activity_other_count = (TextView) convertView.findViewById(R.id.activity_other_count);
			holder.yinzhang_iv = (ImageView) convertView.findViewById(R.id.yinzhang_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (null != activityLists) {
			bitmapUtils.display(holder.activity_prize_url,
					Constants.ImageUrl + activityLists.get(position).getActivity_prize_url() + Constants.ImageSuffix);
			holder.activity_name.setText(activityLists.get(position).getActivity_name());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf
					.format(new Date(Long.valueOf(activityLists.get(position).getActivity_start_time() + "000")))
					.substring(0, 10);
			holder.activity_start_time.setText(time + "发布");
			holder.activity_sponsor.setText(activityLists.get(position).getActivity_sponsor_name());
			holder.item_pro.setMax(activityLists.get(position).getActivity_total_count());
			holder.item_pro.setProgress(activityLists.get(position).getActivity_play_count());
			holder.activity_play_count.setText(activityLists.get(position).getActivity_play_count() + "");
			holder.activity_total_count.setText(activityLists.get(position).getActivity_total_count() + "");
			holder.activity_other_count.setText(activityLists.get(position).getActivity_total_count()
					- activityLists.get(position).getActivity_play_count() + "");

			if (activityLists.get(position).getActivity_status().equals("3")) {
				holder.yinzhang_iv.setVisibility(View.VISIBLE);
			} else {
				holder.yinzhang_iv.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		CircleImageView1 activity_prize_url;
		TextView activity_name;
		TextView activity_start_time;
		TextView activity_sponsor;
		ProgressBar item_pro;
		TextView activity_play_count;
		TextView activity_total_count;
		TextView activity_other_count;
		ImageView yinzhang_iv;
	}
}