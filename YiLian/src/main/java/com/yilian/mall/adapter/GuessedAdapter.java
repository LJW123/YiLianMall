package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.GuessedNumberEntity.GuessedNumberEntityList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GuessedAdapter extends BaseAdapter {

	private List<GuessedNumberEntityList> guessedNumberList;
	private Context context;

	public GuessedAdapter(Context context, List<GuessedNumberEntityList> guessedNumberList) {
		this.context = context;
		this.guessedNumberList = guessedNumberList;
	}

	@Override
	public int getCount() {
		if (guessedNumberList.size() != 0) {
			return guessedNumberList.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return guessedNumberList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_guessed_number, null);
			holder.guessed_circle_iv = (ImageView) convertView.findViewById(R.id.guessed_circle_iv);
			holder.guessed_my_number_tv = (TextView) convertView.findViewById(R.id.guessed_my_number_tv);
			holder.guessed_number_times = (TextView) convertView.findViewById(R.id.guessed_number_times);
			holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (null != guessedNumberList) {
			if (position == 0) {
				
				holder.guessed_circle_iv.setBackgroundResource(R.drawable.guessed_circle_shap_green);
				
			}else {
				holder.guessed_circle_iv.setBackgroundResource(R.drawable.guessed_circle_shap_yellow);
			}
			holder.guessed_my_number_tv.setText(guessedNumberList.get(position).guess_figure);
			holder.guessed_number_times.setText(guessedNumberList.get(position).guess_count);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(new Date(Long.valueOf(guessedNumberList.get(position).guess_time + "000")));
			holder.time_tv.setText(time);
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView guessed_circle_iv;
		TextView guessed_my_number_tv;
		TextView guessed_number_times;
		TextView time_tv;
	}
}
